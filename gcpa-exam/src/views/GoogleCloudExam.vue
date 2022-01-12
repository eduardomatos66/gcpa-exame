<template>
  <v-container fluid id="googleCloudExam">
    <v-card fluid class="v-card-margin-bottom">
      <div style="padding: 10px">This is under construction</div>
    </v-card>

    <v-card v-if="this.questions.length" fluid class="v-card-margin-bottom">
      <Question :question="this.questions[400]" @selectedOption="printSelected" />
    </v-card>

  </v-container>
</template>

<script>
import QuestionService from "../services/QuestionService";
import Question from "@/components/question/Question";

export default {
  name: "GoogleCloudExam",
  components: {
    Question
  },
  data: () => ({
    questions: {},
    loading: true,
  }),
  computed: {
  },
  mounted: function () {
    this.getQuestions();
  },
  methods: {
    /**
     * Gets all the issues from the database.
     */
    async getQuestions() {
      const {data} = await QuestionService.getQuestions();
      this.questions = data;
      this.loading = false;
    },

    printSelected(selectedOption) {
      console.log("This is the selected option: " + selectedOption.correct);
    }
  }

}
</script>

<style scoped>
.v-card-margin-bottom{
  margin-bottom: 10px;
}
</style>