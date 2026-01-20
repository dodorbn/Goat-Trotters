import { createRouter, createWebHistory } from 'vue-router'
import Home from '../pages/Home.vue'
import About from '../pages/About.vue'
import Questions from '../pages/Questions.vue'
import Stats from '../pages/Stats.vue'
import Contact from '../pages/Contact.vue'
import Legal from '../pages/Legal.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Home',
      component: Home
    },
    {
      path: '/about',
      name: 'About',
      component: About
    },
    {
      path: '/questions',
      name: 'Questions',
      component: Questions
    },
    {
      path: "/stats",
      name: "Stats",
      component: Stats
    },
    {
      path: "/contact",
      name: "Contact",
      component: Contact
    },
    {
      path : "/mentions-legales",
      name : "Legal",
      component : Legal
    }
  ],
})

export default router
