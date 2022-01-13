<template>
  <span class="question-padding-left">{{ option }}</span>
</template>

<script>
export default {
  name: "Answer",
  props: {
    option: String,
    isCorrect: Boolean
  }
}
</script>

<style scoped>
.question-padding-left {
  padding-left: 10px;
}
</style>