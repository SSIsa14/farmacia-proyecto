import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MedicamentoListComponent } from './medicamento-list.component';
import { provideMockStore, MockStore } from '@ngrx/store/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

describe('MedicamentoListComponent', () => {
  let component: MedicamentoListComponent;
  let fixture: ComponentFixture<MedicamentoListComponent>;
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
        MedicamentoListComponent, // componente standalone
        HttpClientTestingModule   // provee HttpClient para MedicamentoService
      ],
      providers: [
        provideMockStore({ initialState }), // mock del store
        {
          provide: ActivatedRoute,
          useValue: {
            paramMap: of({ get: (key: string) => null }) // mock simple
          }
        }
      ]
    }).compileComponents();

    store = TestBed.inject(MockStore);
    fixture = TestBed.createComponent(MedicamentoListComponent);
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
