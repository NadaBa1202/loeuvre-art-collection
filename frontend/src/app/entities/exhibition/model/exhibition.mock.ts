import { Exhibition } from './exhibition';

export const EXHIBITIONS: Exhibition[] = [
  {
    id: 1,
    title: 'Shadows & Gold: The Chiaroscuro Era',
    description: "Une immersion magistrale dans les ténèbres lumineuses du Caravage, de Rembrandt et de Georges de La Tour.",
    imageUrl: 'images/shadows-gold-the-chiaroscuro-era.png',
    badge: 'Exposition En Cours',
    location: 'Aile Richelieu • Crypte A',
    dateLabel: "Jusqu'au 28 Mai 2025",
    ctaLabel: 'Consulter le livret',
  },
  {
    id: 2,
    title: 'Flemish Botanica: The Vanitas Codex',
    description: "Natures mortes, allégories de l'impermanence et symbolique secrète des peintres de la Guilde de Saint-Luc.",
    imageUrl: 'images/flemish-botanica-the-vanitas-codex.png',
    badge: "Prêt d'Honneur",
    location: 'Pavillon Sully • Galerie Haute',
    dateLabel: 'Vernissage le 14 Juin',
    ctaLabel: 'Réserver invitation',
  },
  {
    id: 3,
    title: 'Marbles of Antiquity: Eternal Pantheon',
    description: "Statuaire hellénistique et chefs-d'œuvre romains immortalisés en numérisation volumétrique 3D ultra-précise.",
    imageUrl: 'images/marbles-of-antiquity-eternal-pantheon.png',
    badge: 'Rétrospective Permanente',
    location: 'Rotonde des Marbres',
    dateLabel: 'Accès Permanent',
    ctaLabel: 'Parcours numérique',
  },
];