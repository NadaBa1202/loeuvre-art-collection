import { Component, input } from '@angular/core';
import { Exhibition } from '../../model/exhibition';

@Component({
  selector: 'app-exhibition-card',
  templateUrl: './exhibition-card.html',
  styleUrl: './exhibition-card.css',
})
export class ExhibitionCard {
  exhibition = input.required<Exhibition>();
}