import http from "../http-common";

/**
 * The QuestionService.
 * This service is responsible for requesting data to the backend.
 */
class QuestionService {
    /**
     * Gets all the questions present on the database.
     * @returns {*} a list containing all the questions present on the database.
     */
    getQuestions() {
        return http.get("/");
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
}

export default new QuestionService();