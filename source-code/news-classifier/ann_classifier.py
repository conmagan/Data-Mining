import time
import numpy as np
import pandas as pd
from keras import layers
from keras import models
from keras import optimizers
from keras.utils import np_utils
from keras.wrappers.scikit_learn import KerasClassifier
from sklearn.model_selection import KFold
from sklearn.model_selection import cross_val_score
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder

seed, dim, data_dir = 13, 100, 'data/'
np.random.seed(seed)


def encode(y):
    encoder = LabelEncoder()
    encoder.fit(y)
    encoded_Y = encoder.transform(y)
    return np_utils.to_categorical(encoded_Y)


def load(path, dim):
    print('Loading data', path, '...')
    data_set = pd.read_csv(path, header=None).values
    x, y = data_set[:, 0:dim].astype(float), data_set[:, dim]
    return x, encode(y)


def evaluate(model, seed, x, y, ep, bz):
    print('+ Evaluating by K-fold validation ... ', end='', flush=True)
    estimator = KerasClassifier(build_fn=model, epochs=ep, batch_size=bz, verbose=0)
    estimator.fit(x, y)
    results = cross_val_score(estimator, x, y, cv=KFold(n_splits=5, shuffle=True, random_state=seed))
    print('%.2f%%' % (results.mean() * 100))


def create_model():
    model = models.Sequential([
        layers.Dense(dim, input_dim=dim, activation='relu'),
        layers.Dropout(0.5),
        layers.Dense(64, activation='relu'),
        layers.Dropout(0.5),
        layers.Dense(32, activation='relu'),
        layers.Dropout(0.5),
        layers.Dense(16, activation='relu'),
        layers.Dropout(0.5),
        layers.Dense(5, activation='softmax')
    ])
    adam = optimizers.Adam(lr=0.001)
    model.compile(loss='categorical_crossentropy', optimizer=adam, metrics=['accuracy'])
    return model


x, y = load(data_dir + "data.csv", dim)
x_train, x_test, y_train, y_test = train_test_split(x, y, test_size=0.2, random_state=seed)

print('+ Training ANN model ...')
start_time = time.time()
ep, bz = 1000, 256
model = create_model()
model.fit(x_train, y_train, epochs=ep, batch_size=bz, verbose=2)
print('+ Training time: ', time.time() - start_time, 's')
evaluate(create_model, seed, x, y, ep, bz)

prediction = model.predict(x_test)
count_acc = 0
for p_index in range(len(prediction)):
    if prediction[p_index].argmax() == np.argmax(y_test[p_index]):
        count_acc = count_acc + 1
print('-> Accuracy in test data: ', count_acc / len(prediction))
