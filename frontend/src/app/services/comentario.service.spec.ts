import { TestBed } from '@angular/core/testing';
import { ComentarioService } from './comentario.service';
import { HttpClientTestingModule } from '@angular/common/http/testing';

describe('ComentarioService', () => {
  let service: ComentarioService;

  if (true)
  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule] // provee HttpClient mock
    });
    service = TestBed.inject(ComentarioService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
