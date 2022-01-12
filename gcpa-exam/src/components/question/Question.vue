<template>
  <v-card style="padding-left: 10px">
    <div class="font-weight-bold question-title-padding">
      {{ this.question.title }}
    </div>
    <v-list v-for="answer in this.question.questionOptionList"
            :key="answer.optionDescription"
            class="radio-button-padding-left"
    >
      <input type="radio"
             :id="answer.optionDescription"
             :value="answer"
             v-text="answer.optionDescription"
             v-model="selectedOption"
             @change="onRadioButtonChange($event)"
      >
      <label for="answer.optionDescription"
             class="d-inline-flex">
        <Answer :option="answer.optionDescription"
                :is-correct="answer.correct" />
      </label>
    </v-list>
  </v-card>
</template>

<script>
import Answer from "@/components/question/Answer";
export default {
  name: "Question",
  components: {
    Answer
  },
  props: {
    question: Object
  },
  data: () => ({
    selectedOption: Object
  }),
  methods: {
    onRadioButtonChange() {
      this.$emit("selectedOption", this.selectedOption);
    }
  }
}
</script>

<style scoped>
.question-title-padding {
  padding: 10px;
}
.radio-button-padding-left {
  padding-left: 10px;
}
</style>