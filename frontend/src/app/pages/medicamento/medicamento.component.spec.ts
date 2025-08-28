import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { MedicamentoComponent } from './medicamento.component';
import { of } from 'rxjs';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { provideMockStore, MockStore } from '@ngrx/store/testing';

describe('MedicamentoComponent', () => {
  let component: MedicamentoComponent;
  let fixture: ComponentFixture<MedicamentoComponent>;
  let store: MockStore;

  const initialState = {
    medicamentos: {
      list: [],
      loading: false,
      error: null
    }
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        MedicamentoComponent,
        HttpClientTestingModule // provee HttpClient
      ],
      providers: [
        provideMockStore({ initialState }), // mock del store
        {
          provide: ActivatedRoute,
          useValue: {
            params: of({ id: 1 }) // mock de parámetros de ruta
          }
        }
      ]
    }).compileComponents();

    store = TestBed.inject(MockStore);
    fixture = TestBed.createComponent(MedicamentoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges(); // inicializa ngOnInit
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should have store injected', () => {
    expect(store).toBeTruthy();
  });
});
