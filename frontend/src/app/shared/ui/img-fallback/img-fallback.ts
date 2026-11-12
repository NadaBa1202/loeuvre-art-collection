import { Directive } from '@angular/core';

@Directive({
  selector: 'img[appImgFallback]',
  host: { '(error)': 'onError($event)' },
})
export class ImgFallback {
  private readonly fallback = 'images/placeholder.svg';

  onError(event: Event) {
    const img = event.target as HTMLImageElement;
    if (!img.src.endsWith(this.fallback)) {
      img.src = this.fallback;
    }
  }
}