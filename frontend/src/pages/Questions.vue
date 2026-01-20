<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import axios from 'axios'
import { useRouter } from "vue-router"

const router = useRouter()
const questions = ref<any[]>([])
const loading = ref(false)
const error = ref('')
const success = ref(false)
const currentStep = ref(1)

const API_URL = import.meta.env.VITE_API_BASE_URL
  ? `${import.meta.env.VITE_API_BASE_URL}/api`
  : '/api'

const countryMapping: Record<string, string> = {
  'Allemagne': 'Germany', 'Autriche': 'Austria', 'Belgique': 'Belgium', 'Bulgarie': 'Bulgaria',
  'Chypre': 'Cyprus', 'Croatie': 'Croatia', 'Danemark': 'Denmark', 'Espagne': 'Spain',
  'Estonie': 'Estonia', 'Finlande': 'Finland', 'France': 'France', 'Grèce': 'Greece',
  'Hongrie': 'Hungary', 'Irlande': 'Ireland', 'Italie': 'Italy', 'Lettonie': 'Latvia',
  'Lituanie': 'Lithuania', 'Luxembourg': 'Luxembourg', 'Malte': 'Malta', 'Pays-Bas': 'Netherlands',
  'Pologne': 'Poland', 'Portugal': 'Portugal', 'République tchèque': 'Czech Republic',
  'Roumanie': 'Romania', 'Royaume-Uni': 'United Kingdom', 'Slovaquie': 'Slovakia',
  'Slovénie': 'Slovenia', 'Suède': 'Sweden'
}

const displayCountries = Object.keys(countryMapping).sort()

const form = ref({
  country: '',
  category: '',
  age: '' as string | number,
  gender: ''
})

const answers = ref<any[]>([])
const userId = ref(localStorage.getItem('userId') || 'user_' + Math.random().toString(36).substr(2, 9))

onMounted(async () => {
  if (!localStorage.getItem('userId')) {
    localStorage.setItem('userId', userId.value)
  }

  try {
    const response = await axios.get(`${API_URL}/questions`)
    questions.value = response.data
    answers.value = new Array(questions.value.length).fill(undefined)
  } catch (err) {
    console.error('Erreur chargement questions:', err)
    error.value = "Impossible de charger le sondage. Vérifiez que le Backend tourne."
  }
})

const getOptions = (question: any) => {
  if (!question.possibleAnswers) return []
  return question.possibleAnswers.split(';')
}

const isSelected = (index: number, option: string) => {
  const currentAnswer = answers.value[index]
  if (!currentAnswer) return false
  return String(currentAnswer).split(';').includes(option)
}

const toggleOption = (index: number, option: string) => {
  const currentAnswer = answers.value[index] || ''
  let selectedOptions = currentAnswer ? String(currentAnswer).split(';') : []

  if (selectedOptions.includes(option)) {
    selectedOptions = selectedOptions.filter(o => o !== option)
  } else {
    selectedOptions.push(option)
  }
  answers.value[index] = selectedOptions.join(';')
}

const isFormValid = computed(() => {
  return form.value.country && form.value.category && form.value.age && form.value.gender
})

const isAnswersComplete = computed(() => {
  return answers.value.every(a => a !== undefined && a !== null && a !== '')
})

function nextStep() {
  if (currentStep.value === 1 && isFormValid.value) {
    currentStep.value = 2
    window.scrollTo({ top: 0, behavior: 'smooth' })
  }
}

function previousStep() {
  currentStep.value = 1
}

const submitForm = async () => {
  if (!isAnswersComplete.value) return
  loading.value = true
  error.value = ''

  try {
    const promises = questions.value.map((q, index) => {
      const englishCountry = countryMapping[form.value.country] || form.value.country
      const finalAnswer = String(answers.value[index])

      const payload = {
        userId: userId.value,
        age: Number(form.value.age),
        country: englishCountry,
        category: form.value.category,
        gender: form.value.gender,
        answer: finalAnswer,
        question: { id: q.id }
      }
      return axios.post(`${API_URL}/responses`, payload)
    })

    await Promise.all(promises)
    success.value = true
    setTimeout(() => { router.push('/stats') }, 2500)

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
          <select v-model="form.country" class="custom-input">
            <option value="" disabled>-- Choisir --</option>
            <option v-for="c in displayCountries" :key="c" :value="c">{{ c }}</option>
          </select>
        </div>

        <div class="input-group">
          <label>Âge</label>
          <input type="number" v-model="form.age" min="15" max="99" placeholder="Ex: 25" class="custom-input no-arrow">
        </div>

        <div class="row">
          <div class="input-group half">
            <label>Genre</label>
            <select v-model="form.gender" class="custom-input">
              <option value="" disabled>-- Choisir --</option>
              <option>Homme</option>
              <option>Femme</option>
              <option>Autre</option>
            </select>
          </div>
          <div class="input-group half">
            <label>Statut</label>
            <select v-model="form.category" class="custom-input">
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

          <div v-if="q.type === 'SCORE'" class="score-container">
            <div class="score-buttons">
              <button
                v-for="n in 10"
                :key="n"
                class="score-btn"
                :class="{ selected: answers[index] === n }"
                @click="answers[index] = n"
              >
                {{ n }}
              </button>
            </div>
            <div class="score-labels">
              <span>Pas du tout 😡</span>
              <span>Absolument 😍</span>
            </div>
          </div>

          <div v-else-if="q.type === 'COUNTRY' || q.text.toLowerCase().includes('pays')" class="input-group">
            <select v-model="answers[index]" class="custom-input">
              <option :value="undefined" disabled>-- Choisir un pays --</option>
              <option v-for="(enName, frName) in countryMapping" :key="enName" :value="enName">
                {{ frName }}
              </option>
            </select>
          </div>

          <div v-else-if="q.type === 'CHOICE'" class="choices-container">
            <button
              v-for="opt in getOptions(q)"
              :key="opt"
              class="choice-btn"
              :class="{ selected: answers[index] === opt }"
              @click="answers[index] = opt"
            >
              {{ opt }}
            </button>
          </div>

          <div v-else-if="q.type === 'MULTIPLE_CHOICE'" class="multiple-choice-wrapper">
            <p class="hint-text">Vous pouvez sélectionner plusieurs réponses :</p>
            <div class="choices-container">
              <button
                v-for="opt in getOptions(q)"
                :key="opt"
                class="choice-btn"
                :class="{ selected: isSelected(index, opt) }"
                @click="toggleOption(index, opt)"
              >
                {{ opt }}
              </button>
            </div>
          </div>

          <div v-else-if="q.type === 'TEXT'" class="text-container">
            <textarea
              v-model="answers[index]"
              rows="2"
              placeholder="Écrivez votre réponse ici..."
              class="custom-input"
            ></textarea>
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
.page-container {
  min-height: 100vh;
  padding: 2rem;
  color: white;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.header { text-align: center; margin-bottom: 2rem; }
.header h1 { color: #D4AF37; margin-bottom: 0.5rem; }
.header p { color: #aaa; }

.survey-box {
  background: #1b1336;
  width: 100%; max-width: 700px;
  padding: 2rem;
  border-radius: 16px;
  box-shadow: 0 10px 30px rgba(0,0,0,0.5);
  border: 1px solid #333;
}

.steps-indicator { display: flex; align-items: center; justify-content: center; margin-bottom: 1.5rem; }
.step-dot { width: 35px; height: 35px; border-radius: 50%; background: #333; color: #777; display: flex; justify-content: center; align-items: center; font-weight: bold; transition: all 0.3s; }
.step-dot.active { background: #D4AF37; color: #1b1336; box-shadow: 0 0 10px #D4AF37; }
.step-dot.done { background: #4CAF50; color: white; }
.step-line { height: 2px; width: 50px; background: #333; margin: 0 10px; }
.step-title { text-align: center; font-size: 1.5rem; margin-bottom: 2rem; color: #fff; }

.input-group { margin-bottom: 1.5rem; display: flex; flex-direction: column; }
.input-group label { margin-bottom: 0.5rem; color: #D4AF37; font-size: 0.9rem; }

/* --- 🧹 NETTOYAGE CSS POUR UNIFORMISER --- */

/* Style de base commun pour Inputs, Selects et Textarea */
.custom-input {
  background: #2a2245;
  border: 1px solid #444;
  color: white;
  padding: 12px;
  border-radius: 8px;
  font-size: 1rem;
  outline: none;
  width: 100%;

  /* Pour s'assurer que Select et Input font la même taille */
  height: 48px;
  box-sizing: border-box;
}

/* Suppression du style par défaut des SELECT pour mettre le notre */
select.custom-input {
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-image: url("data:image/svg+xml;charset=US-ASCII,%3Csvg%20xmlns%3D%22http%3A%2F%2Fwww.w3.org%2F2000%2Fsvg%22%20width%3D%22292.4%22%20height%3D%22292.4%22%3E%3Cpath%20fill%3D%22%23D4AF37%22%20d%3D%22M287%2069.4a17.6%2017.6%200%200%200-13-5.4H18.4c-5%200-9.3%201.8-12.9%205.4A17.6%2017.6%200%200%200%200%2082.2c0%205%201.8%209.3%205.4%2012.9l128%20127.9c3.6%203.6%207.8%205.4%2012.8%205.4s9.2-1.8%2012.8-5.4L287%2095c3.5-3.5%205.4-7.8%205.4-12.8%200-5-1.9-9.2-5.5-12.8z%22%2F%3E%3C%2Fsvg%3E");
  background-repeat: no-repeat;
  background-position: right 1rem top 50%;
  background-size: 0.65rem auto;
  padding-right: 2.5rem;
  cursor: pointer;
}

/* Suppression des flèches moches de l'INPUT NUMBER */
input[type=number].no-arrow::-webkit-outer-spin-button,
input[type=number].no-arrow::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}
input[type=number].no-arrow {
  -moz-appearance: textfield; /* Firefox */
}

/* Focus */
.custom-input:focus { border-color: #D4AF37; }

/* Textarea spécifique */
textarea.custom-input {
  resize: vertical;
  min-height: 80px;
  font-family: inherit;
  height: auto; /* Le textarea garde sa hauteur auto */
}

/* --- FIN DU NETTOYAGE --- */

.row { display: flex; gap: 1rem; }
.half { flex: 1; }

.question-block {
  margin-bottom: 2.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #333;
}
.question-block:last-child { border-bottom: none; }
.question-block h3 { margin-bottom: 1rem; font-weight: 400; font-size: 1.1rem; color: #eee; }

.score-container { display: flex; flex-direction: column; gap: 0.5rem; }
.score-buttons { display: flex; gap: 5px; justify-content: space-between; flex-wrap: wrap; }
.score-btn {
  width: 40px; height: 40px;
  border-radius: 8px; border: 1px solid #444; background: #2a2245; color: white;
  cursor: pointer; transition: all 0.2s; font-weight: bold;
}
.score-btn:hover { border-color: #D4AF37; }
.score-btn.selected { background: #D4AF37; color: #1b1336; border-color: #D4AF37; transform: scale(1.1); }
.score-labels { display: flex; justify-content: space-between; font-size: 0.8rem; color: #888; margin-top: 5px; }

.choices-container { display: grid; grid-template-columns: 1fr; gap: 10px; }
@media(min-width: 500px) { .choices-container { grid-template-columns: 1fr 1fr; } }

.choice-btn {
  background: #2a2245; border: 1px solid #444; color: white;
  padding: 12px; border-radius: 8px; cursor: pointer; text-align: left; transition: all 0.2s;
}
.choice-btn:hover { background: #352b57; border-color: #777; }
.choice-btn.selected { background: #D4AF37; color: #1b1336; border-color: #D4AF37; font-weight: bold; }

.btn-primary {
  background: #D4AF37; color: #1b1336; border: none; padding: 12px 24px;
  border-radius: 50px; font-weight: bold; cursor: pointer; font-size: 1rem; transition: transform 0.2s;
}
.btn-primary:hover:not(:disabled) { transform: translateY(-2px); box-shadow: 0 5px 15px rgba(212,175,55,0.4); }
.btn-primary:disabled { background: #555; color: #888; cursor: not-allowed; }
.btn-primary.full { width: 100%; margin-top: 1rem; }
.btn-secondary { background: transparent; color: #aaa; border: 1px solid #555; padding: 12px 24px; border-radius: 50px; cursor: pointer; }
.btn-secondary:hover { color: white; border-color: white; }
.buttons-row { display: flex; justify-content: space-between; margin-top: 2rem; }

.error-msg { color: #ff6b6b; text-align: center; margin-top: 1rem; }
.success-card { text-align: center; background: #1b1336; padding: 3rem; border-radius: 16px; border: 1px solid #444; }
.icon-circle { font-size: 3rem; margin-bottom: 1rem; }
.redirect-text { font-size: 0.9rem; color: #888; margin-top: 1rem; font-style: italic; }

.fade-in { animation: fadeIn 0.5s ease; }
@keyframes fadeIn { from { opacity: 0; transform: translateY(10px); } to { opacity: 1; transform: translateY(0); } }

.hint-text { font-size: 0.85rem; color: #aaa; margin-bottom: 0.5rem; font-style: italic; }
</style>
