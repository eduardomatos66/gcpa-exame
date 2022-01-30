import http from "../http-common";

/**
 * The QuestionService.
 * This service is responsible for requesting data to the backend.
 */
class QuestionService {
    /**
     * Gets all the questions present on the database.
     *
     * @returns {*} a list containing all the questions present on the database.
     */
    getAllQuestions() {
        return http.get("/");
    }

    /**
     * Gets a fixed number of questions.
     *
     * @param amount the total number of questions to get.
     * @returns {*} the list of questions with the amount specified.
     */
    getQuestions(amount, label) {
        if (label == 'All') {
            return http.get("/exam?questionsNumber=" + amount)
        }
        return http.get("/exam?questionsNumber=" + amount + "&label=" + label)
    }

    /**
     * Gets the respective question for the given ID.
     *
     * @param questionId the Question's ID.
     * @returns {*} the respective question for the given ID.
     */
    getQuestion(questionId) {
        return http.get("/" + questionId + "/")
    }

    /**
     * Gets questions labels available.
     *
     * @returns {*} the labels available for registered question.
     */
    getLabels() {
        return http.get("/labels")
    }
}

export default new QuestionService();