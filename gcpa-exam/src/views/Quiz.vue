<template>
  <div class="quiz-questions" style="padding=250px">
    <img style="opacity: 0.75; max-width: 20%; height: auto;" alt="GCPA logo" src="../assets/logo-gcpa.png">
    <QuizQuestions />
  </div>
</template>

<script>
// @ is an alias to /src
import QuizQuestions from '@/components/QuizQuestions.vue'

export default {
  name: 'Quiz',
  components: {
    QuizQuestions
  }
}
</script>
