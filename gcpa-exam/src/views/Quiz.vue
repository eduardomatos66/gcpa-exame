<template>
  <v-container class="quiz-questions align-center">
    <v-row class="text-center">
      <v-col cols="12">
        <v-img
            :src="require('../assets/logo-gcpa.png')"
            class="my-lg-n16"
            contain
            height="180"
            style="opacity: 60%"
        />
      </v-col>

      <v-col>
        <QuizQuestions class="my-3" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
// @ is an alias to /src
import QuizQuestions from '@/components/question/QuizQuestions.vue'

export default {
  name: 'Quiz',
  components: {
    QuizQuestions
  }
}
</script>
