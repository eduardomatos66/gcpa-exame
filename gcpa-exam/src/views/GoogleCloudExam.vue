<template>
  <v-container fluid id="googleCloudExam">

    <v-main>
      <Quiz></Quiz>
    </v-main>

  </v-container>
</template>

<script>
import Quiz from "@/views/Quiz";

export default {
  name: "GoogleCloudExam",
  components: {
    Quiz,
  },
  data: () => ({
    questions: {},
    loading: true,
  }),
  computed: {
  },
  mounted: function () {

  },
  methods: {

  }

}
</script>

<style scoped>
.v-card-margin-bottom{
  margin-bottom: 10px;
}
</style>