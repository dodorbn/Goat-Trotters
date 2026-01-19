<script setup lang="ts">
import { ref } from 'vue'
import emailjs from '@emailjs/browser'

const form = ref({
  name: '',
  subject: '',
  message: ''
})

const loading = ref(false)
const successMessage = ref(false)
const errorMessage = ref('')

const sendEmail = async () => {
  loading.value = true
  errorMessage.value = ''
  successMessage.value = false

  const SERVICE_ID = 'service_qz0a6pp'
  const TEMPLATE_ID = 'template_p449wsg'
  const PUBLIC_KEY = '1C1VC_Z_SQjvmXAtS'

  const templateParams = {
    name: form.value.name,
    subject: form.value.subject,
    message: form.value.message
  }

  try {
    await emailjs.send(SERVICE_ID, TEMPLATE_ID, templateParams, PUBLIC_KEY)

    successMessage.value = true
    form.value = { name: '', subject: '', message: '' }
  } catch (error) {
    console.error('Erreur envoi email:', error)
    errorMessage.value = "Une erreur est survenue. Veuillez réessayer plus tard."
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page-container">

    <header class="header">
      <h1>Contactez <span class="highlight">Goat Trotters</span></h1>
      <p class="subtitle">Une question ? Une suggestion ? Écrivez-nous !</p>
      <div class="separator"></div>
    </header>

    <div class="content-wrapper">

      <div class="info-card fade-in-left">
        <h2>Nos Coordonnées</h2>
        <p class="info-text">
          Nous sommes toujours ravis d'échanger avec notre communauté. Que ce soit pour un partenariat,
          une question sur nos données ou juste pour dire bonjour.
        </p>

        <div class="contact-item">
          <div class="icon-circle">📧</div>
          <div>
            <h3>Email</h3>
            <a href="mailto:goat.trotters@gmail.com" class="link">goat.trotters@gmail.com</a>
          </div>
        </div>

        <div class="contact-item">
          <div class="icon-circle">🌍</div>
          <div>
            <h3>Réseaux</h3>
            <p>Retrouvez-nous sur Instagram, TikTok et YouTube.</p>
          </div>
        </div>

        <div class="decoration-circle"></div>
      </div>

      <form @submit.prevent="sendEmail" class="contact-form fade-in-right">

        <div v-if="successMessage" class="alert success">
          ✅ Message envoyé avec succès ! Nous vous répondrons vite.
        </div>

        <div v-if="errorMessage" class="alert error">
          ❌ {{ errorMessage }}
        </div>

        <div class="form-group">
          <label for="name">Votre Nom</label>
          <input
            id="name"
            v-model="form.name"
            type="text"
            placeholder="Ex: Jean Dupont"
            required
          />
        </div>

        <div class="form-group">
          <label for="subject">Sujet</label>
          <select id="subject" v-model="form.subject" required>
            <option value="" disabled selected>-- Choisissez un sujet --</option>
            <option>Demande d'information</option>
            <option>Proposition de partenariat</option>
            <option>Problème technique</option>
            <option>Autre</option>
          </select>
        </div>

        <div class="form-group">
          <label for="message">Message</label>
          <textarea
            id="message"
            v-model="form.message"
            rows="5"
            placeholder="Votre message ici..."
            required
          ></textarea>
        </div>

        <button type="submit" class="btn-submit" :disabled="loading">
          {{ loading ? 'Envoi en cours...' : 'Envoyer le message' }}
          <span v-if="!loading" class="arrow">→</span>
        </button>
      </form>

    </div>
  </div>
</template>

<style scoped>

.page-container {
  min-height: 100vh;
  background-color: #1b1336;
  color: white;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  padding: 4rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.header {
  text-align: center;
  margin-bottom: 3rem;
}

.header h1 {
  font-size: 2.5rem;
  font-weight: 300;
  margin-bottom: 0.5rem;
}

.highlight {
  color: #D4AF37;
  font-weight: 700;
}

.subtitle {
  color: #aaa;
  font-size: 1.1rem;
}

.separator {
  width: 60px;
  height: 3px;
  background: #D4AF37;
  margin: 1.5rem auto;
  border-radius: 2px;
}

.content-wrapper {
  display: grid;
  grid-template-columns: 1fr 1.5fr;
  gap: 3rem;
  max-width: 1000px;
  width: 100%;
}

.info-card {
  background: #2a2245;
  padding: 2.5rem;
  border-radius: 16px;
  border: 1px solid #444;
  position: relative;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.info-card h2 {
  color: #D4AF37;
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
}

.info-text {
  color: #ccc;
  line-height: 1.6;
  font-size: 0.95rem;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.icon-circle {
  width: 50px;
  height: 50px;
  background: rgba(212, 175, 55, 0.1);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 1.5rem;
  border: 1px solid #D4AF37;
}

.contact-item h3 {
  font-size: 1rem;
  color: white;
  margin-bottom: 0.2rem;
}

.link {
  color: #D4AF37;
  text-decoration: none;
  font-weight: bold;
  transition: color 0.3s;
}
.link:hover { color: white; }

.decoration-circle {
  position: absolute;
  bottom: -50px;
  right: -50px;
  width: 150px;
  height: 150px;
  background: radial-gradient(circle, rgba(212,175,55,0.1) 0%, rgba(0,0,0,0) 70%);
  border-radius: 50%;
  pointer-events: none;
}

.contact-form {
  background: #2a2245;
  padding: 2.5rem;
  border-radius: 16px;
  border: 1px solid #444;
  box-shadow: 0 10px 30px rgba(0,0,0,0.3);
}

.form-group {
  margin-bottom: 1.5rem;
  display: flex;
  flex-direction: column;
}

.form-group label {
  color: #D4AF37;
  margin-bottom: 0.5rem;
  font-size: 0.9rem;
  font-weight: 500;
}

input, select, textarea {
  background: #1b1336;
  border: 1px solid #444;
  color: white;
  padding: 12px;
  border-radius: 8px;
  font-size: 1rem;
  font-family: inherit;
  outline: none;
  transition: border-color 0.3s;
}

input:focus, select:focus, textarea:focus {
  border-color: #D4AF37;
}

textarea {
  resize: vertical;
}

.btn-submit {
  background: #D4AF37;
  color: #1b1336;
  border: none;
  padding: 12px 24px;
  border-radius: 50px;
  font-size: 1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  width: 100%;
  margin-top: 1rem;
}

.btn-submit:hover:not(:disabled) {
  background: white;
  color: #D4AF37;
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(212,175,55,0.2);
}

.btn-submit:disabled {
  background: #555;
  color: #888;
  cursor: not-allowed;
}

.arrow { font-size: 1.2rem; }

.alert {
  padding: 1rem;
  border-radius: 8px;
  margin-bottom: 1.5rem;
  font-weight: 500;
  text-align: center;
}

.alert.success {
  background-color: rgba(76, 175, 80, 0.2);
  border: 1px solid #4CAF50;
  color: #4CAF50;
}

.alert.error {
  background-color: rgba(244, 67, 54, 0.2);
  border: 1px solid #f44336;
  color: #f44336;
}

.fade-in-left { animation: fadeInLeft 0.8s ease; }
.fade-in-right { animation: fadeInRight 0.8s ease; }

@keyframes fadeInLeft {
  from { opacity: 0; transform: translateX(-30px); }
  to { opacity: 1; transform: translateX(0); }
}

@keyframes fadeInRight {
  from { opacity: 0; transform: translateX(30px); }
  to { opacity: 1; transform: translateX(0); }
}

@media (max-width: 850px) {
  .content-wrapper {
    grid-template-columns: 1fr;
  }
  .header h1 { font-size: 2rem; }
}
</style>
