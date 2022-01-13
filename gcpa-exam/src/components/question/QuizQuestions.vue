<template>
  <v-container class="d-flex justify-content-center quiz-card">

    <div v-if="showScore">
      <b-card
          title="Results"
          class="gcpa-card">
        <span>You Scored {{ score }} of {{ questions.length }}</span>
      </b-card>
    </div>

    <div class="card-q" v-else>
      <span v-if="!startQuiz">
        <b-card
            :img-src="require('../../assets/photo-1524995997946-a1c2e315a42f.jpeg')"
            img-alt="Image"
            img-top
            title="Google Cloud Platform Associate - Practice Exam"
            class="mb-2 gcpa-card">
          <b-button @click="startQuizFunc()">Start Quiz</b-button>
        </b-card>
      </span>

      <span v-else>
        <b-card
            title="Google Cloud Platform Associate - Practice Exam"
            class="mb-2 gcpa-card"
        >
          <div class="question-counter">
            <span style="font-size: 28px;">
              <strong>{{ countDownDisplay }}</strong>
            </span><br>
            Question No.{{ currentQuestion + 1 }} of {{ questions.length }}
          </div>

          <br>

          <b-progress
              variant="warning"
              :max="120"
              :value="countDown"
              height="4px"
          ></b-progress>

          <div class="question-title">
            {{ questions[currentQuestion].title }}
          </div>

          <div class="answer-section">
            <b-button :key="index" v-for="(option, index) in questions[currentQuestion].questionOptionList" @click="handleAnswerClick(option.correct)" class="ans-option-btn" variant="primary">{{option.optionDescription}}</b-button>
          </div>
        </b-card>
      </span>
    </div>
  </v-container>
</template>

<script>
import QuestionService from "@/services/QuestionService";

export default {
  data: () => ({
    currentQuestion: 0,
    showScore: false,
    score:0,
    countDown : 120,
    countDownDisplay: "",
    timer:null,
    startQuiz: false,
    questions: [],
  }),

  methods: {
    async getQuestions() {
      try {
        const {data} = await QuestionService.getQuestions(50)
        this.questions = data;
        console.log(this.questions);
        this.countDown = 120 * this.questions.length;
      } catch (error) {
        console.log(error);
      }
    },

    startQuizFunc() {
      this.startQuiz = true
      this.countDownTimer()
    },

    handleAnswerClick(isCorrect) {
      let nextQuestion = this.currentQuestion + 1;

      if(isCorrect) {
        this.score = this.score + 1;
      }

      if(nextQuestion < this.questions.length) {
        this.currentQuestion = nextQuestion;
        // this.$store.state.questionAttended = this.currentQuestion;
        // localStorage.setItem('qattended', this.currentQuestion)
      }
      else {
        // localStorage.removeItem('qattended')
        this.showScore = true;
        clearTimeout(this.timer);
        // localStorage.setItem('testComplete',this.showScore)
      }
    },
    countDownTimer() {
      if(this.countDown > 0) {
        this.timer = setTimeout(() => {
          this.countDown -= 1
          let hour = Math.floor(this.countDown / 60 / 60);
          let min = Math.floor(this.countDown / 60) - (hour * 60);
          let sec = this.countDown - (min * 60) - (hour * 3600) ;

          if(sec < 10) {
            sec = '0' + sec;
          }

          this.countDownDisplay = hour + 'h ' + min + 'min ' + sec + 's'
          this.countDownTimer()
        }, 1000)
      }
      else {
        this.handleAnswerClick(false)
      }
    },
  },
  mounted: function() {
    //  alert(this.$store.state.questionAttended)
    //    this.showScore = localStorage.getItem('testComplete') || false
    //    this.currentQuestion = localStorage.getItem('qattended') || 0
    //    this.countDownTimer()
    //    this.fetchQuiz()
    this.getQuestions()
  }
}
</script>

<style>
.card{
  min-width: 100%;
  border-radius: 15px;
  padding: 20px;
  box-shadow: 10px 10px 42px 0 rgba(0, 0, 0, 0.75);
  margin: 20px;
}

.gcpa-card {
  max-width: 300rem;
}

.card-q{
  min-width: 60%;
}

.answer-section {
  width: 100%;
  align-items: center;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.timer-text {
  background: rgb(230, 153, 12);
  padding: 15px;
  margin-top: 20px;
  margin-right: 20px;
  border: 5px solid rgb(255, 189, 67);
  border-radius: 15px;
  text-align: center;
}

.card-img, .card-img-top {
  border-top-left-radius: calc(0.25rem - 1px);
  border-top-right-radius: calc(0.25rem - 1px);
  height: 350px;
}

.ans-option-btn {
  width: 80%;
  font-size: 16px;
  color: #ffffff;
  background-color: #252d4a;
  border-radius: 15px;
  display: flex;
  justify-content: flex-start;
  align-items: center;
  border: 5px solid #234668;
  cursor: pointer;
  margin: 5px;
  padding: 10px 10px 10px 5%;
}

.question-counter {
  font-size: 14px;
  text-align: right;
}

.question-title {
  font-size: 20px;
  margin-bottom: 25px;
}

.quiz-card {
  padding-left: 15%;
  padding-right: 15%;
}
</style>
