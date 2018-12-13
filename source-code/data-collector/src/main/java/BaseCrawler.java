public class BaseCrawler {

    public static State mState = null;

    public static class State {
        private int currentThread;
        private int currentPage;
        private int currentTopic;

        public State() {
            this.currentThread = -1;
            this.currentPage = 0;
            this.currentTopic = -1;
        }

        public State(int currentThread, int currentPage, int currentTopic) {
            this.currentThread = currentThread;
            this.currentPage = currentPage;
            this.currentTopic = currentTopic;
        }

        @Override
        public String toString() {
            return "{" + currentThread + ", " + currentPage + ", " + currentTopic + '}';
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public int getCurrentTopic() {
            return currentTopic;
        }

        public void setCurrentTopic(int currentTopic) {
            this.currentTopic = currentTopic;
        }
    }
}
