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
    { src: 'images/l-astronome-rudit-baroque-chef-d-uvre.png', alt: "L'astronome érudit", caption: 'Inventaire National • N° 084' },
    { src: 'images/bdfeff00-585f-4edd-a485-22e42998e9c9_629x799.jpg', alt: "L'astronome érudit", caption: 'Inventaire National • N° 084' },
    { src: 'images/The-Scream-casein-cardboard-Edvard-Munch-National-1893.webp', alt: "L'astronome érudit", caption: 'Inventaire National • N° 084' },
    { src: 'images/self-portrait-with-necklace-of-thorns-1940.jpg!Portrait.jpg', alt: "L'astronome érudit", caption: 'Inventaire National • N° 084' },
    { src: 'images/Mona_Lisa,_by_Leonardo_da_Vinci,_from_C2RMF_retouched.jpg', alt: "L'astronome érudit", caption: 'Inventaire National • N° 084' },
  ];
}