import { Component } from '@angular/core';
import { Button } from '@shared/ui/button';
import { SectionHeader } from '@shared/ui/section-header';

@Component({
  selector: 'app-ecosystem',
  imports: [SectionHeader, Button],
  templateUrl: './ecosystem.html',
  styleUrl: './ecosystem.css',
})
export class Ecosystem {
  readonly pillars = [
    { n: '01', title: 'Récolement Décennal', text: "Workflow complet conforme à la circulaire ministérielle. Pointage physique, constat d'état comparatif et génération automatique des procès-verbaux de récolement." },
    { n: '02', title: 'Télémétrie Climatique', text: "Sondes IoT intégrées en réserves et salles d'exposition. Surveillance temps réel de la luxmétrie, de l'hygrométrie relative (HR 50% ± 5%) et de la température." },
    { n: '03', title: 'Imagerie Gigapixel', text: "Visualisation multispectrale (lumière rasante, réflectographie infrarouge, fluorescence UV) avec zoom profond pour l'analyse des vernis et craquelures." },
    { n: '04', title: 'Convois & Assurances', text: "Gestion de la chaîne du clou à clou : devis de transport spécialisé en caisses isothermes, protocoles de douane et polices d'assurance « tous risques clou à clou »." },
  ];
}