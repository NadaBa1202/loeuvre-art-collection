import { Component } from '@angular/core';

@Component({
  selector: 'app-about',
  templateUrl: './about.html',
  styleUrl: './about.css',
})
export class About {
  readonly metrics = [
    { value: '14 800+', label: 'Œuvres Répertoriées' },
    { value: '99.8%', label: 'Indice de Conservation' },
    { value: 'CIDOC', label: 'Conformité CRM-v7' },
  ];

  readonly fan = [
    { src: '', alt: 'Portrait baroque des maîtres anciens' },
    { src: '', alt: 'Paysage idéalisé et forêt éternelle' },
    { src: '', alt: 'Nature morte florale, école hollandaise' },
    { src: '', alt: 'Bouquet botanique baroque et clair-obscur' },
    { src: 'images/l-astronome-rudit-baroque-chef-d-uvre.png', alt: "L'astronome érudit", caption: 'Inventaire National • N° 084' },
  ];
}