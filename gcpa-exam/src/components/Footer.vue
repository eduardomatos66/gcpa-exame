<template>
    <v-footer app id="footer">
        <v-col cols="12" class="text-center footer-panel footer">
            <div class="footer-text">ERME</div>
            <div class="footer-text">© {{getCurrentYear()}} ERME, Inc. All Rights reserved.</div>
        </v-col>
    </v-footer>
</template>

<script>
    export default {
        name: "Footer",
        methods: {
            getCurrentYear() {
                return new Date().getFullYear();
            }
        }
    }
</script>

<style scoped>
.footer-text {
  font-size: 15px;
}

.footer {
  padding: 0;
}
</style>