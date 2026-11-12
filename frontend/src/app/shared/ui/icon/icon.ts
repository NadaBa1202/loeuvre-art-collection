import { Component, input } from '@angular/core';

export type IconName = 'home' | 'grid' | 'user' | 'image' | 'users' | 'logout' | 'plus' | 'edit' | 'trash';

@Component({
  selector: 'app-icon',
  template: `<svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2"
                  stroke-linecap="round" stroke-linejoin="round" aria-hidden="true">
               <use [attr.href]="'images/icons.svg#' + name()" />
             </svg>`,
  styles: ':host { display: inline-flex; } .icon { width: 1.2em; height: 1.2em; }',
})
export class Icon {
  name = input.required<IconName>();
}