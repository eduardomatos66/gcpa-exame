<template>
  <v-container class="d-flex justify-content-center my-md-n16 quiz-card">

    <div v-if="showScore">
      <b-card
          :title="'Results (with remaining time: ' + this.lastCountDown + ')'"
          class="gcpa-card">
        <span>You Scored {{ score }} of {{ questions.length }}</span>
        <Report :questions="questions" />
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
          <b-button @click="startQuizFunc()">Start Exam </b-button>
          <div style="margin-top: 20px">
            <p>Number of questions:</p>
            <input v-model="numberOfQuestions" style="text-align: center; font-size: 20px;"/>
          </div>
        </b-card>
      </span>

      <span v-else>
        <b-card
            title="Google Cloud Platform Associate - Practice Exam"
            class="mb-2 gcpa-card"
        >
          <div class="question-counter">
            <span style="font-size: 14px; position: absolute; left: 10%">            
              Question No.{{ currentQuestion + 1 }} of {{ questions.length }}
            </span>
            <span style="font-size: 16px; position: absolute; left: 75%;">
              <strong>{{ countDownDisplay }}</strong>
            </span>            
          </div>
          <br>

          <b-progress
              :max="120"
              :value="countDown"
              height="8px"
          ></b-progress>

          <div class="question-title" v-if="questions.length">
            {{ questions[currentQuestion].title }}
          </div>

          <div class="answer-section" v-if="questions.length">
            <b-button :key="index"
                      v-for="(option, index) in questions[currentQuestion].questionOptionList"
                      @click="handleAnswerClick(option.correct, index)"
                      class="ans-option-btn"
                      :class="{ 'selected-answer': questions[currentQuestion].chosen !== undefined && questions[currentQuestion].chosen === index }"
                      variant="primary">
                      {{ option.optionDescription }}
            </b-button>
          </div>
          <div class="d-inline-flex">
            <b-button :disabled="currentQuestion === 0" class="margin-right" @click="goToPreviousQuestion">Previous</b-button>
            <b-button v-show="currentQuestion < questions.length - 1" @click="goToNextQuestion">Next</b-button>
            <b-button v-show="currentQuestion === questions.length - 1" @click="finishExam">Finish exam</b-button>
          </div>
        </b-card>
      </span>
    </div>
  </v-container>
</template>

<script>
import QuestionService from "@/services/QuestionService";
import Report from "@/components/question/Report";

export default {
  data: () => ({
    numberOfQuestions: 50,
    currentQuestion: 0,
    showScore: false,
    score: 0,
    countDown : 120,
    countDownDisplay: "",
    lastCountDown: "",
    timer:null,
    startQuiz: false,
    questions: []
  }),
  components: {
    Report,
  },
  methods: {
    async getQuestions() {
      try {
        const {data} = await QuestionService.getQuestions(this.numberOfQuestions)
        this.questions = data;
        this.countDown = 120 * this.questions.length;
      } catch (error) {
        console.log(error);
      }
    },

    startQuizFunc() {
      this.getQuestions()
      
      this.startQuiz = true
      this.countDownTimer()
    },

    handleAnswerClick(isCorrect, chosen) {
      this.questions[this.currentQuestion]["chosen"] = chosen;
      this.questions[this.currentQuestion]["correctlyAnswered"] = isCorrect;
    },

    goToNextQuestion() {
      let nextQuestionNum = this.currentQuestion + 1;

      if(nextQuestionNum < this.questions.length) {
        this.currentQuestion = nextQuestionNum;
        // this.$store.state.questionAttended = this.currentQuestion;
        // localStorage.setItem('qattended', this.currentQuestion)
      }
    },

    goToPreviousQuestion() {
      let previousQuestion = this.currentQuestion - 1;

      if(previousQuestion >= 0) {
        this.currentQuestion = previousQuestion;
        // this.$store.state.questionAttended = this.currentQuestion;
        // localStorage.setItem('qattended', this.currentQuestion)
      }
    },

    finishExam() {
      // localStorage.removeItem('qattended')
      let currentScore = 0;
      this.showScore = true;
      this.lastCountDown = this.countDownDisplay;
      clearTimeout(this.timer);

      this.questions.forEach(function (question) {
        if(question.correctlyAnswered) {
          currentScore++;
        }
      });
      this.score = currentScore;
      // localStorage.setItem('testComplete',this.showScore)
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
        this.handleAnswerClick(false, -1)
      }
    },
  },

  mounted: function() {

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
  background: rgb(12, 143, 230);
  padding: 15px;
  margin-right: 20px;
  border: 5px solid rgb(4, 0, 255);
  border-radius: 15px;
  text-align: center;
  margin-top: 15px;
  margin-bottom: 15px;
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

.selected-answer {
  background-color: #007bfe;
}

.question-counter {
  font-size: 14px;
  text-align: left;
}

.question-title {
  font-size: 20px;
  margin-bottom: 25px;
}

.quiz-card {
  padding-left: 15%;
  padding-right: 15%;
}

.margin-right {
  margin-right: 20px;
}
</style>
