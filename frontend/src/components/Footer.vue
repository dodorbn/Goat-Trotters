<script setup lang="ts">
import { ref } from 'vue';

const socialLinks = ref([
  {
    name: 'Instagram',
    url: 'https://instagram.com/goat.trotters',
    icon: new URL('../assets/icons/instagram.png', import.meta.url).href
  },
  {
    name: 'Tiktok',
    url: 'https://tiktok.com/@goat.trotters',
    icon: new URL('../assets/icons/tiktok.png', import.meta.url).href
  },
  {
    name: 'YouTube',
    url: 'https://youtube.com/@goattrotters',
    icon: new URL('../assets/icons/youtube.png', import.meta.url).href
  }
]);

const currentYear = new Date().getFullYear();
</script>

<template>
  <footer class="footer-container">
    <div class="footer-content">

      <div class="footer-top">
        <div class="brand-column">
          <h3 class="brand-title">GOAT TROTTERS<span class="dot">.</span></h3>
          <p class="brand-tagline">Explorer. Analyser. Préserver.</p>
        </div>

        <div class="social-column">
          <h4>Suivez l'aventure</h4>
          <div class="social-grid">
            <a v-for="social in socialLinks"
               :key="social.name"
               :href="social.url"
               target="_blank"
               rel="noopener noreferrer"
               class="social-card">
              <div class="icon-wrapper">
                <img :src="social.icon" :alt="social.name" class="icon" />
              </div>
              <span class="social-name">{{ social.name }}</span>
            </a>
          </div>
        </div>
      </div>

      <div class="separator"></div>

      <div class="footer-bottom">
        <p class="copyright">© {{ currentYear }} Goat Trotters. Tous droits réservés.</p>
        <div class="legal-links">
          <router-link to="/mentions-legales">Mentions légales</router-link>
          <span class="divider">•</span>
          <router-link to="/mentions-legales">Confidentialité</router-link>
          <span class="divider">•</span>
          <router-link to="/contact">Contact</router-link>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer-container {
  background-color: #120c24;
  border-top: 1px solid #D4AF37;
  padding: 3rem 2rem 1.5rem;
  color: #fff;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  margin-top: auto;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-top {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-wrap: wrap;
  gap: 2rem;
  margin-bottom: 2rem;
}

.brand-title {
  font-size: 1.8rem;
  color: white;
  font-weight: 700;
  letter-spacing: 1px;
  margin-bottom: 0.5rem;
}

.dot {
  color: #D4AF37;
}

.brand-tagline {
  color: #888;
  font-size: 0.9rem;
  letter-spacing: 0.5px;
}

.social-column h4 {
  color: #D4AF37;
  font-size: 1rem;
  text-transform: uppercase;
  margin-left: 1rem;
  margin-bottom: 1rem;
  letter-spacing: 1px;
}

.social-grid {
  display: flex;
  gap: 1rem;
  flex-wrap: wrap;
}

.social-card {
  display: flex;
  align-items: center;
  gap: 0.8rem;
  background: rgba(255, 255, 255, 0.05);
  padding: 0.6rem 1.2rem;
  border-radius: 50px;
  border: 1px solid #333;
  text-decoration: none;
  transition: all 0.3s ease;
}

.icon {
  width: 20px;
  height: 20px;
  object-fit: contain;
}

.social-name {
  color: #ccc;
  font-size: 0.9rem;
  font-weight: 500;
  transition: color 0.3s;
}

.social-card:hover {
  background: #D4AF37;
  border-color: #D4AF37;
  transform: translateY(-3px);
  box-shadow: 0 5px 15px rgba(212, 175, 55, 0.3);
}

.social-card:hover .social-name {
  color: #120c24;
}

.separator {
  height: 1px;
  background: linear-gradient(90deg, transparent, #333, transparent);
  margin: 2rem 0;
}

.footer-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 1rem;
  font-size: 0.85rem;
  color: #666;
}

.legal-links {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.legal-links a {
  color: #888;
  text-decoration: none;
  transition: color 0.2s;
}

.legal-links a:hover {
  color: #D4AF37;
}

.divider {
  color: #444;
}

@media (max-width: 768px) {
  .footer-top {
    flex-direction: column;
    align-items: center;
    text-align: center;
  }

  .social-grid {
    justify-content: center;
  }

  .footer-bottom {
    flex-direction: column;
    text-align: center;
  }
}
</style>
