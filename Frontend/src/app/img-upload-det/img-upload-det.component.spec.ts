import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImgUploadDetComponent } from './img-upload-det.component';

describe('ImgUploadDetComponent', () => {
  let component: ImgUploadDetComponent;
  let fixture: ComponentFixture<ImgUploadDetComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ImgUploadDetComponent]
    })
    .compileComponents();
    
    fixture = TestBed.createComponent(ImgUploadDetComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
