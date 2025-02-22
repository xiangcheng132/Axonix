<template>
  <router-view />
</template>
  
  <script>
  // JS部分
  import AdminDashboard from './views/AdminDashboard.vue';
  
  export default {
    name: 'App',
    components: {
      AdminDashboard,
    }
  }
  </script>
  
  <style>
  </style>
  