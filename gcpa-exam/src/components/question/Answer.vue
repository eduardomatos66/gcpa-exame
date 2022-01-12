<template>
  <p>{{ option }}</p>
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

</style>