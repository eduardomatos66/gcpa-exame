<template>
    <v-app id="gcpa-exam">
        <Header/>

        <v-content id="main" fluid class="fill-height">
            <router-view/>
        </v-content>
        <Footer/>
    </v-app>
</template>

<script>
    import Header from "./components/Header";
    import Footer from "./components/Footer";

    export default {
        created () {
            this.$vuetify.theme.light = true
        },
        components: {
            Footer,
            Header
        },
        data: () => ({
        })
    }
</script>

<style scoped>
</style>