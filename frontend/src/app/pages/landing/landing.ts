import { Component } from '@angular/core';
import { Hero } from '@widgets/hero';
import { About } from '@widgets/about';
import { Events } from '@widgets/events';
import { Ecosystem } from '@widgets/ecosystem';

@Component({
  selector: 'app-landing',
  imports: [Hero, About, Events, Ecosystem],
  templateUrl: './landing.html',
})
export class Landing {}