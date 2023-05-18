import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBonReception, NewBonReception } from '../bon-reception.model';

export type PartialUpdateBonReception = Partial<IBonReception> & Pick<IBonReception, 'id'>;

type RestOf<T extends IBonReception | NewBonReception> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

export type RestBonReception = RestOf<IBonReception>;

export type NewRestBonReception = RestOf<NewBonReception>;

export type PartialUpdateRestBonReception = RestOf<PartialUpdateBonReception>;

export type EntityResponseType = HttpResponse<IBonReception>;
export type EntityArrayResponseType = HttpResponse<IBonReception[]>;

@Injectable({ providedIn: 'root' })
export class BonReceptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bon-receptions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bonReception: NewBonReception): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bonReception);
    return this.http
      .post<RestBonReception>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(bonReception: IBonReception): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bonReception);
    return this.http
      .put<RestBonReception>(`${this.resourceUrl}/${this.getBonReceptionIdentifier(bonReception)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(bonReception: PartialUpdateBonReception): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(bonReception);
    return this.http
      .patch<RestBonReception>(`${this.resourceUrl}/${this.getBonReceptionIdentifier(bonReception)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestBonReception>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestBonReception[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBonReceptionIdentifier(bonReception: Pick<IBonReception, 'id'>): number {
    return bonReception.id;
  }

  compareBonReception(o1: Pick<IBonReception, 'id'> | null, o2: Pick<IBonReception, 'id'> | null): boolean {
    return o1 && o2 ? this.getBonReceptionIdentifier(o1) === this.getBonReceptionIdentifier(o2) : o1 === o2;
  }

  addBonReceptionToCollectionIfMissing<Type extends Pick<IBonReception, 'id'>>(
    bonReceptionCollection: Type[],
    ...bonReceptionsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bonReceptions: Type[] = bonReceptionsToCheck.filter(isPresent);
    if (bonReceptions.length > 0) {
      const bonReceptionCollectionIdentifiers = bonReceptionCollection.map(
        bonReceptionItem => this.getBonReceptionIdentifier(bonReceptionItem)!
      );
      const bonReceptionsToAdd = bonReceptions.filter(bonReceptionItem => {
        const bonReceptionIdentifier = this.getBonReceptionIdentifier(bonReceptionItem);
        if (bonReceptionCollectionIdentifiers.includes(bonReceptionIdentifier)) {
          return false;
        }
        bonReceptionCollectionIdentifiers.push(bonReceptionIdentifier);
        return true;
      });
      return [...bonReceptionsToAdd, ...bonReceptionCollection];
    }
    return bonReceptionCollection;
  }

  protected convertDateFromClient<T extends IBonReception | NewBonReception | PartialUpdateBonReception>(bonReception: T): RestOf<T> {
    return {
      ...bonReception,
      dateCreation: bonReception.dateCreation?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restBonReception: RestBonReception): IBonReception {
    return {
      ...restBonReception,
      dateCreation: restBonReception.dateCreation ? dayjs(restBonReception.dateCreation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestBonReception>): HttpResponse<IBonReception> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestBonReception[]>): HttpResponse<IBonReception[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
