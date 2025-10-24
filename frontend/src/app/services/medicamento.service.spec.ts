import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing'; // IMPORTAR esto
import { MedicamentoService } from './medicamento.service';

describe('MedicamentoService', () => {
  let service: MedicamentoService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule], // así Angular sabe cómo proveer HttpClient
    });
    service = TestBed.inject(MedicamentoService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
