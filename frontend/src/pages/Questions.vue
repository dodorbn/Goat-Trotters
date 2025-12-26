<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from "vue-router"

// --- 1. CONFIGURATION & DATAS ---
const router = useRouter()
const questions = ref<any[]>([])
const loading = ref(false)
const error = ref('')
const success = ref(false)
const currentStep = ref(1)

// Mapping pour convertir l'affichage (FR) vers la BDD (EN)
const countryMapping: Record<string, string> = {
  'Allemagne': 'Germany',
  'Autriche': 'Austria',
  'Belgique': 'Belgium',
  'Bulgarie': 'Bulgaria',
  'Chypre': 'Cyprus',
  'Croatie': 'Croatia',
  'Danemark': 'Denmark',
  'Espagne': 'Spain',
  'Estonie': 'Estonia',
  'Finlande': 'Finland',
  'France': 'France',
  'Grèce': 'Greece',
  'Hongrie': 'Hungary',
  'Irlande': 'Ireland',
  'Italie': 'Italy',
  'Lettonie': 'Latvia',
  'Lituanie': 'Lithuania',
  'Luxembourg': 'Luxembourg',
  'Malte': 'Malta',
  'Pays-Bas': 'Netherlands',
  'Pologne': 'Poland',
  'Portugal': 'Portugal',
  'République tchèque': 'Czech Republic',
  'Roumanie': 'Romania',
  'Royaume-Uni': 'United Kingdom',
  'Slovaquie': 'Slovakia',
  'Slovénie': 'Slovenia',
  'Suède': 'Sweden'
}

// Liste affichée dans le select (triée alphabétiquement)
const displayCountries = Object.keys(countryMapping).sort()

// Données du formulaire
const form = ref({
  country: '',
  category: '',
  age: '' as string | number, // Pour gérer l'input vide
  gender: ''
})

const answers = ref<string[]>([]) // Stocke les "Oui"/"Non"

// --- 2. LOGIQUE UTILISATEUR ---
const userId = ref(localStorage.getItem('userId') || 'user_' + Math.random().toString(36).substr(2, 9))

onMounted(async () => {
  // Sauvegarde l'ID si pas existant
  if (!localStorage.getItem('userId')) {
    localStorage.setItem('userId', userId.value)
  }

  // Chargement des questions
  try {
    const response = await axios.get('http://localhost:8080/api/questions')
    questions.value = response.data
    // Initialiser le tableau de réponses avec la même taille
    answers.value = new Array(questions.value.length).fill(undefined)
  } catch (err) {
    console.error('Erreur chargement questions:', err)
    error.value = "Impossible de charger le sondage."
  }
})

// Validation
const isFormValid = computed(() => {
  return form.value.country && form.value.category && form.value.age && form.value.gender
})

const isAnswersComplete = computed(() => {
  return answers.value.every(a => a !== undefined && a !== null)
})

// Navigation
function nextStep() {
  if (currentStep.value === 1 && isFormValid.value) {
    currentStep.value = 2
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

function previousStep() {
  currentStep.value = 1
}

// --- 3. ENVOI DES DONNÉES ---
const submitForm = async () => {
  if (!isAnswersComplete.value) return

  loading.value = true
  error.value = ''

  try {
    // On prépare toutes les requêtes en parallèle
    const promises = questions.value.map((q, index) => {

      // Conversion du pays en Anglais pour le Backend
      const englishCountry = countryMapping[form.value.country] || form.value.country

      const payload = {
        userId: userId.value,
        age: Number(form.value.age),
        country: englishCountry, // ✅ Envoi en anglais (ex: "Spain")
        category: form.value.category,
        gender: form.value.gender,
        answer: answers.value[index],
        question: { id: q.id } // ✅ Lien correct avec l'ID
      }

      return axios.post('http://localhost:8080/api/responses', payload)
    })

    await Promise.all(promises)

    success.value = true
    setTimeout(() => {
      router.push('/stats') // Redirection auto
    }, 2500)

  } catch (err) {
    console.error(err)
    error.value = "Une erreur est survenue lors de l'enregistrement."
  } finally {
    loading.value = false
  }
}
</script>

<template>
  <div class="page-container">

    <header class="header">
      <h1>Votre voix en Europe 🇪🇺</h1>
      <p>Aidez-nous à cartographier les opinions de demain.</p>
    </header>

    <div v-if="success" class="success-card">
      <div class="icon-circle">🎉</div>
      <h2>Merci !</h2>
      <p>Vos réponses ont été enregistrées.</p>
      <p class="redirect-text">Redirection vers les statistiques...</p>
    </div>

    <div v-else class="survey-box">

      <div class="steps-indicator">
        <div class="step-dot" :class="{ active: currentStep === 1, done: currentStep > 1 }">1</div>
        <div class="step-line"></div>
        <div class="step-dot" :class="{ active: currentStep === 2 }">2</div>
      </div>

      <div class="step-title">
        {{ currentStep === 1 ? 'Qui êtes-vous ?' : 'Vos Opinions' }}
      </div>

      <div v-if="currentStep === 1" class="step-content fade-in">
        <div class="input-group">
          <label>Pays de résidence</label>
          <select v-model="form.country">
            <option value="" disabled>-- Choisir --</option>
            <option v-for="c in displayCountries" :key="c" :value="c">{{ c }}</option>
          </select>
        </div>

        <div class="input-group">
          <label>Âge</label>
          <input type="number" v-model="form.age" min="15" max="99" placeholder="Ex: 25">
        </div>

        <div class="row">
          <div class="input-group half">
            <label>Genre</label>
            <select v-model="form.gender">
              <option value="" disabled>-- Choisir --</option>
              <option>Homme</option>
              <option>Femme</option>
              <option>Autre</option>
            </select>
          </div>
          <div class="input-group half">
            <label>Statut</label>
            <select v-model="form.category">
              <option value="" disabled>-- Choisir --</option>
              <option>Étudiant(e)</option>
              <option>Employé(e)</option>
              <option>Cadre</option>
              <option>Ouvrier(ère)</option>
              <option>Retraité(e)</option>
              <option>Sans emploi</option>
            </select>
          </div>
        </div>

        <button class="btn-primary full" :disabled="!isFormValid" @click="nextStep">
          Suivant
        </button>
      </div>

      <div v-else class="step-content fade-in">
        <div v-if="questions.length === 0" class="loading-text">Chargement des questions...</div>

        <div v-for="(q, index) in questions" :key="q.id" class="question-block">
          <h3>{{ q.text }}</h3>
          <div class="options-container">
            <label class="option-card" :class="{ selected: answers[index] === 'Oui' }">
              <input type="radio" :name="'q'+q.id" value="Oui" v-model="answers[index]">
              <span class="emoji">👍</span>
              <span>Oui</span>
            </label>

            <label class="option-card" :class="{ selected: answers[index] === 'Non' }">
              <input type="radio" :name="'q'+q.id" value="Non" v-model="answers[index]">
              <span class="emoji">👎</span>
              <span>Non</span>
            </label>
          </div>
        </div>

        <div v-if="error" class="error-msg">{{ error }}</div>

        <div class="buttons-row">
          <button class="btn-secondary" @click="previousStep">Retour</button>
          <button class="btn-primary" :disabled="!isAnswersComplete || loading" @click="submitForm">
            {{ loading ? 'Envoi...' : 'Valider mes réponses' }}
          </button>
        </div>
      </div>

    </div>
  </div>
</template>

<style scoped>
/* --- STYLE GLOBAL "DARK THEME" --- */
.page-container {
  min-height: 100vh;
  padding: 2rem;
  color: white;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.header {
  text-align: center;
  margin-bottom: 2rem;
}
.header h1 { color: #D4AF37; margin-bottom: 0.5rem; }
.header p { color: #aaa; }

/* --- BOITE PRINCIPALE --- */
.survey-box {
  background: #1b1336; /* Même fond que le dashboard */
  width: 100%;
  max-width: 600px;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.5);
  border: 1px solid #333;
}

/* --- INDICATEUR D'ÉTAPES --- */
.steps-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 1.5rem;
}
.step-dot {
  width: 35px; height: 35px;
  border-radius: 50%;
  background: #333;
  color: #777;
  display: flex; justify-content: center; align-items: center;
  font-weight: bold;
  transition: all 0.3s;
}
.step-dot.active { background: #D4AF37; color: #1b1336; box-shadow: 0 0 10px #D4AF37; }
.step-dot.done { background: #4CAF50; color: white; }
.step-line { height: 2px; width: 50px; background: #333; margin: 0 10px; }

.step-title {
  text-align: center;
  font-size: 1.5rem;
  margin-bottom: 2rem;
  color: #fff;
}

/* --- FORMULAIRE --- */
.input-group { margin-bottom: 1.5rem; display: flex; flex-direction: column; }
.input-group label { margin-bottom: 0.5rem; color: #D4AF37; font-size: 0.9rem; }
.input-group input, .input-group select {
  background: #2a2245;
  border: 1px solid #444;
  color: white;
  padding: 12px;
  border-radius: 8px;
  font-size: 1rem;
  outline: none;
}
.input-group input:focus, .input-group select:focus { border-color: #D4AF37; }

.row { display: flex; gap: 1rem; }
.half { flex: 1; }

/* --- QUESTIONS (ETAPE 2) --- */
.question-block {
  margin-bottom: 2rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #333;
}
.question-block:last-child { border-bottom: none; }
.question-block h3 { margin-bottom: 1rem; font-weight: 400; }

.options-container { display: flex; gap: 1rem; }
.option-card {
  flex: 1;
  background: #2a2245;
  padding: 15px;
  border-radius: 8px;
  border: 1px solid #444;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  transition: all 0.2s;
}
.option-card:hover { background: #352b57; }
.option-card.selected {
  background: #D4AF37;
  color: #1b1336;
  font-weight: bold;
  border-color: #D4AF37;
}
.option-card input { display: none; } /* Cacher le bouton radio moche */
.emoji { font-size: 1.2rem; }

/* --- BOUTONS --- */
.btn-primary {
  background: #D4AF37; color: #1b1336;
  border: none; padding: 12px 24px;
  border-radius: 50px; font-weight: bold; cursor: pointer;
  font-size: 1rem; transition: transform 0.2s;
}
.btn-primary:hover:not(:disabled) { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(212,175,55,0.4); }
.btn-primary:disabled { background: #555; color: #888; cursor: not-allowed; }
.btn-primary.full { width: 100%; margin-top: 1rem; }

.btn-secondary {
  background: transparent; color: #aaa;
  border: 1px solid #555; padding: 12px 24px;
  border-radius: 50px; cursor: pointer;
}
.btn-secondary:hover { color: white; border-color: white; }

.buttons-row { display: flex; justify-content: space-between; margin-top: 2rem; }

/* --- MESSAGES --- */
.error-msg { color: #ff6b6b; text-align: center; margin-top: 1rem; }
.success-card {
  text-align: center; background: #1b1336; padding: 3rem;
  border-radius: 16px; border: 1px solid #444;
}
.icon-circle { font-size: 3rem; margin-bottom: 1rem; }
.redirect-text { font-size: 0.9rem; color: #888; margin-top: 1rem; font-style: italic; }

/* --- ANIMATION --- */
.fade-in { animation: fadeIn 0.5s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }
</style>
