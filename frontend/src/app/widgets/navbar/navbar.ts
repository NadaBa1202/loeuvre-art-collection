import { Component, signal } from '@angular/core';
import { Button } from '@shared/ui/button';

@Component({
  selector: 'app-navbar',
  imports: [Button],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css',
})
export class Navbar {
  readonly links = [
    { label: 'Sanctuaire', href: '#about' },
    { label: 'Salons', href: '#events' },
    { label: 'Régie', href: '#ecosystem' },
  ];

  readonly open = signal(false);

  toggle() { this.open.update(v => !v); }
  close() { this.open.set(false); }
}