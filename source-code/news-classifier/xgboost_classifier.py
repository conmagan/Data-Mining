import time

import numpy as np
from sklearn.metrics import accuracy_score
from sklearn.model_selection import train_test_split
from xgboost import XGBClassifier


def evaluate(data, test_size):
    dim = len(data[0]) - 1
    x, y = data[:, 0:dim], data[:, dim]
    sum_score, prv_acc, fold = 0.0, 0.0, 1
    x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=test_size, random_state=fold)
    model = XGBClassifier()
    model.fit(x_train, y_train)
    predictions = [round(p) for p in model.predict(x_test)]
    sum_score += accuracy_score(y_test, predictions)
    acc = sum_score / fold
    print('-> Accuracy on test data:', acc)


print('+ Evaluation using XGBoost ...')
start_time = time.time()
evaluate(np.loadtxt('data/data.csv', delimiter=','), 0.2)
print('+ Training time: ', time.time() - start_time, 's')
