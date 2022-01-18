<template>
    <v-card>
        <div :key="index" v-for="(question, index) in questions" class="question-padding-left">
            <div class="question-title">{{ question.title }}</div>

            <b-button :key="index2" v-for="(option, index2) in question.questionOptionList" class="ans-option-btn"
            v-bind:style="option.correct ? {backgroundColor: '#076b00'} : question.chosen === index2  ? {backgroundColor: '#bd2600'} : {backgroundColor: '#6a84e3'}" variant="primary">{{option.optionDescription}}</b-button>

            <div class="explanation-text"> {{question.explanation}} </div>
        </div>
    </v-card>
</template>

<script>
export default {
  name: "Report",
  props: {
      questions: Array,
  },
  data: () => ({
    timer: null,
    score: 0
  }),
}

</script>

<style scoped>
.question-padding-left {
  padding-left: 10px;
}
.ans-option-btn {
  font-size: 12px;
  margin-left: 50px;
}

.question-title {
  margin-top: 40px;
  font-size: 13px;
}

.explanation-text {
  background-color: rgb(206, 252, 137);
  font-size: 13px;
}
</style>