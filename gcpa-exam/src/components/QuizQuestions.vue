<template>
  <div class="d-flex justify-content-center quizcard">

    <div v-if="showScore">
        <b-card
        title="Results"
        style="max-width: 20rem;">
        You Scored {{score}} of {{backQuestions.length}}
        </b-card>
    </div>
    <div class="card-q" v-else>
    <span v-if="!startQuiz">
         <b-card
         img-src="../assets/photo-1524995997946-a1c2e315a42f.jpeg"
    img-alt="Image"
    img-top
    title="Google Cloud Platform Associate - Exame Practice"
    style="max-width: 20rem;"
    class="mb-2">
      <b-button @click="startQuizFunc()">Start Quiz</b-button>
    </b-card>
    </span>
    <span v-else>
    <b-card
    title="Google Cloud Platform Associate - Exame Practice"
    style="max-width: 20rem;"
    class="mb-2"

  >
   <div class="question-counter">
      Question No.{{currentQuestion + 1}} of {{backQuestions.length}}
    </div>
    <br>
   <b-progress
        variant="warning"
        :max="120"
        :value="countDown"
        height="4px"
      ></b-progress>

    <div class="question-title">
      {{backQuestions[currentQuestion].title}}
    </div>
    <div class="answer-section">
      <b-button :key="index" v-for="(option, index) in backQuestions[currentQuestion].questionOptionList" @click="handleAnswerClick(option.correct)" class="ans-option-btn" variant="primary">{{option.optionDescription}}</b-button>
    </div>
    <b-card-text>
      <span style="font-size: 40px;"><strong>{{countDown}} </strong></span>
    </b-card-text>
  </b-card>
  </span>
  </div>
  </div>
</template>

<script>
export default {
    data(){
        return {
            currentQuestion: 0,
            showScore: false,
            score:0,
            countDown : 120,
            timer:null,
            startQuiz: false,
            backQuestions: [],
            questions : [],
       }
    },

    methods:{
        async getData() {
          try {
            let response = await fetch("http://localhost:8080/question/exam/20");
            this.backQuestions = await response.json();
            this.countDown = 120 * this.backQuestions.length;
          } catch (error) {
            console.log(error);
          }
        },
        startQuizFunc(){
            this.startQuiz = true
            this.countDownTimer()
        },
        handleAnswerClick(isCorrect){
            clearTimeout(this.timer);
            let nextQuestion = this.currentQuestion + 1;
            if(isCorrect){
                this.score = this.score + 1;
            }
            if(nextQuestion < this.backQuestions.length){
            this.currentQuestion = nextQuestion;
            // this.$store.state.questionAttended = this.currentQuestion;
            // localStorage.setItem('qattended', this.currentQuestion)

            this.countDownTimer();
            }
            else{
                // localStorage.removeItem('qattended')
                this.showScore = true;
                // localStorage.setItem('testComplete',this.showScore)
            }
        },
        countDownTimer() {
                if(this.countDown > 0) {
                    this.timer = setTimeout(() => {
                        this.countDown -= 1
                        this.countDownTimer()
                    }, 1000)
                }
                else{
                    this.handleAnswerClick(false)
                }
            }
    },
     created() {
        //  alert(this.$store.state.questionAttended)
        //    this.showScore = localStorage.getItem('testComplete') || false
        //    this.currentQuestion = localStorage.getItem('qattended') || 0
        //    this.countDownTimer()
        //    this.fetchQuiz()
          this.getData()
        }

}
</script>

<style>
    .card{
        min-width: 100%;
        border-radius: 15px;
        padding: 20px;
        box-shadow: 10px 10px 42px 0px rgba(0, 0, 0, 0.75);
        margin: 20px;
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
      padding: 10px;
      justify-content: flex-start;
      align-items: center;
      border: 5px solid #234668;
      cursor: pointer;
      margin: 5px;
      padding-left: 5%;
    }

    .question-counter {
      font-size: 14px;
      text-align: right;
    }

    .question-title {      
      font-size: 20px;
      margin-bottom: 25px;
    }

    .quizcard {
      padding-left: 15%;
      padding-right: 15%;
    }
</style>
