<template>
  <div>
    <SidebarMenu absolut :drawer="drawer" />
    <v-app-bar app clipped-left :color="getHeaderColor">
      <v-app-bar-nav-icon @click.stop="drawer = !drawer"/>
      <v-toolbar-title v-text="pageTitle" class="white--text" />
      <a class="navbar-brand" href="#" style="position: absolute; left: 95%;">
        <img src="../assets/logo-gcpa.png" height="50">
      </a>
    </v-app-bar>
  </div>
</template>

<script>
import SidebarMenu from "./SidebarMenu";

export default {
  name: "Header",
  components: {
    SidebarMenu
  },
  data: () => ({
    drawer: false,
    pageTitle: "Google Cloud Certified - Associate Cloud Engineer"
  }),
  computed: {
    getHeaderColor: function() {
      let colorArray = ["blue"];

      function getRandomIntInclusive(min, max) {
        min = Math.ceil(min);
        max = Math.floor(max);
        return Math.floor(Math.random() * (max - min + 1)) + min; //The maximum is inclusive and the minimum is inclusive
      }

      let randomIndex = getRandomIntInclusive(0, colorArray.length - 1);

      return colorArray[randomIndex];
    }
    // color: this.colorArray[this.randomIndex]
  }
}
</script>

<style scoped>

</style>