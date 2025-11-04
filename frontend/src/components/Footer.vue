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
  },
  {
    name: 'Spotify',
    url: 'https://open.spotify.com/show/goattrotters',
    icon: new URL('../assets/icons/spotify.png', import.meta.url).href
  }
]);


const currentYear = new Date().getFullYear();
</script>

<template>
  <footer class="footer-container">
    <div class="footer-content">
      <!-- Section supérieure -->
      <div class="footer-sections">
        <!-- Réseaux sociaux -->
        <div class="footer-section">
          <h4>Suivez notre aventure</h4>
          <div class="social-links">
            <a v-for="social in socialLinks"
               :key="social.name"
               :href="social.url"
               target="_blank"
               rel="noopener noreferrer"
               class="social-link">
              <img :src="social.icon" :alt="social.name" class="icon" />
              <span class="social-name">{{ social.name }}</span>
            </a>
          </div>
        </div>
      </div>

      <!-- Ligne de séparation -->
      <div class="separator"></div>

      <!-- Section inférieure -->
      <div class="footer-bottom">
        <p>&copy; {{ currentYear }} Goat Trotters. Tous droits réservés.</p>
        <div class="legal-links">
          <a href="#">Mentions légales</a>
          <span>|</span>
          <a href="#">Politique de confidentialité</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<style scoped>
.footer-container {
  background-color: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
  padding: 2rem;
  border-top: 2px solid #D4AF37;
}

.footer-content {
  max-width: 1200px;
  margin: 0 auto;
}

.footer-sections {
  display: grid;
  grid-template-columns: repeat(1, 1fr);
  gap: 2rem;
  margin-bottom: 2rem;
}

.footer-section {
  padding: 1.5rem;
  background: linear-gradient(145deg, rgba(31, 41, 55, 0.7), rgba(17, 24, 39, 0.7));
  border-radius: 8px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.1);
}

h3 {
  color: #D4AF37;
  font-size: 1.5rem;
  font-weight: bold;
}

h4 {
  color: #D4AF37;
  font-size: 1.2rem;
  margin-bottom: 1rem;
  font-weight: bold;
}

ul {
  list-style: none;
  padding: 0;
}

ul li {
  margin-bottom: 0.5rem;
}

a {
  color: #888;
  text-decoration: none;
  transition: color 0.3s;
}

a:hover {
  color: #D4AF37;
}

.social-links {
  display: flex;
  gap: 1rem;
}

.social-link {
  display: flex;
  align-items: center;
  flex-direction: column;
  gap: 1rem;
  padding: 0.75rem 1.5rem;
  background: rgba(212, 175, 55, 0.1);
  border-radius: 8px;
  width: 100%;
  transition: all 0.3s;
}

.social-link:hover {
  background: rgba(212, 175, 55, 0.2);
  transform: translateX(10px);
}

.icon {
  width: 24px;
  height: 24px;
  object-fit: contain;
  transition: filter 0.3s ease;
  font-size: 1.5rem;
}

.social-name {
  color: #D4AF37;
  font-weight: 500;
}

.separator {
  border-top: 1px solid rgba(212, 175, 55, 0.2);
  margin: 2rem 0;
}

.footer-bottom {
  display: flex;
  justify-content: space-between;
  align-items: center;
  color: #888;
  font-size: 0.9rem;
}

.legal-links {
  display: flex;
  gap: 1rem;
  align-items: center;
}

.legal-links span {
  color: #D4AF37;
}

@media (max-width: 768px) {
  .footer-sections {
    grid-template-columns: 1fr;
  }

  .footer-section {
    text-align: center;
  }

  .social-links {
    width: 100%;
    max-width: 300px;
    margin: 0 auto;
  }

  .social-link:hover {
    transform: translateY(-2px);
  }

  .footer-bottom {
    flex-direction: column;
    gap: 1rem;
    text-align: center;
  }
}
</style>
