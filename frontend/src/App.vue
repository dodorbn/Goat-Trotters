<template>
  <div id="app">
    <Navbar />
    <router-view />
    <Footer />
  </div>
</template>

<script setup>
import Navbar from './components/Navbar.vue'
import Footer from './components/Footer.vue'
</script>

<style>
body {
  font-family: 'Poppins', sans-serif;
  background: #1b1336;
  color: white;
  margin: 0;
}
</style>
