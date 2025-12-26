<script setup lang="ts">
import { computed } from 'vue'
import {
  Chart as ChartJS,
  Title,
  Tooltip,
  Legend,
  BarElement,
  CategoryScale,
  LinearScale
} from 'chart.js'
import { Bar } from 'vue-chartjs'

// 1. Enregistrement des composants Chart.js obligatoires
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend)

// 2. On reçoit les stats depuis le parent
const props = defineProps<{
  stats: any[]
}>()

// 3. Transformation des données pour le Graphique
const chartData = computed(() => {
  // On trie les pays pour mettre ceux qui ont le plus de "Oui" en premier (plus lisible)
  const sortedStats = [...props.stats].sort((a, b) => b.yes - a.yes)

  return {
    labels: sortedStats.map(s => s.country), // Axe X : Les pays
    datasets: [
      {
        label: 'Oui',
        backgroundColor: '#D4AF37', // Ta couleur Dorée
        data: sortedStats.map(s => s.yes),
        borderRadius: 4
      },
      {
        label: 'Non',
        backgroundColor: '#444444', // Gris foncé
        data: sortedStats.map(s => s.no),
        borderRadius: 4
      }
    ]
  }
})

// 4. Options de configuration (Couleurs, Responsive, etc.)
const chartOptions = {
  responsive: true,
  maintainAspectRatio: false,
  plugins: {
    legend: {
      labels: { color: 'white' } // Légende en blanc
    },
    tooltip: {
      callbacks: {
        label: (context: any) => `${context.dataset.label}: ${context.raw}%`
      }
    }
  },
  scales: {
    x: {
      ticks: { color: 'white' },
      grid: { display: false }
    },
    y: {
      beginAtZero: true,
      max: 100, // Echelle fixe de 0 à 100%
      ticks: { color: 'white' },
      grid: { color: '#333' }
    }
  }
}
</script>

<template>
  <div class="chart-wrapper">
    <Bar :data="chartData" :options="chartOptions" />
  </div>
</template>

<style scoped>
.chart-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
  padding: 1rem;
}
</style>
