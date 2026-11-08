import { Component } from '@angular/core';
import { EXHIBITIONS, ExhibitionCard } from '@entities/exhibition';

@Component({
  selector: 'app-events',
  imports: [ExhibitionCard],
  templateUrl: './events.html',
  styleUrl: './events.css',
})
export class Events {
  readonly exhibitions = EXHIBITIONS;
}