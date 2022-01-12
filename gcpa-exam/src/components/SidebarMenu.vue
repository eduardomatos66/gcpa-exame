<template>
  <div>
    <v-navigation-drawer
            v-model="drawer"
            app
            clipped
    >
      <v-list dense v-for="item in items" :key="item.title">
        <v-list-item link :to="item.to">
          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
          <v-list-item-content>
            <v-list-item-title>{{ item.title }}</v-list-item-title>
          </v-list-item-content>
        </v-list-item>
      </v-list>
    </v-navigation-drawer>
  </div>
</template>

<script>
  export default {
    name: 'SidebarMenu',
    props: {
      drawer: Boolean
    },
    data: () => ({
      items: [
        { title: 'Home', icon: 'mdi-home', to: '/' },
        { title: 'Get Exam', icon: 'mdi-note-text-outline', to: '/gcpa-exam' },
        { title: 'About', icon: 'mdi-help-box', to: '/about' },
      ],
    })
  }
</script>
<style scoped>
</style>