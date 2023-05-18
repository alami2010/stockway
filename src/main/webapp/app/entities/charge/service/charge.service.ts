import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import dayjs from 'dayjs/esm';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICharge, NewCharge } from '../charge.model';

export type PartialUpdateCharge = Partial<ICharge> & Pick<ICharge, 'id'>;

type RestOf<T extends ICharge | NewCharge> = Omit<T, 'dateCreation'> & {
  dateCreation?: string | null;
};

export type RestCharge = RestOf<ICharge>;

export type NewRestCharge = RestOf<NewCharge>;

export type PartialUpdateRestCharge = RestOf<PartialUpdateCharge>;

export type EntityResponseType = HttpResponse<ICharge>;
export type EntityArrayResponseType = HttpResponse<ICharge[]>;

@Injectable({ providedIn: 'root' })
export class ChargeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/charges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(charge: NewCharge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(charge);
    return this.http
      .post<RestCharge>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  update(charge: ICharge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(charge);
    return this.http
      .put<RestCharge>(`${this.resourceUrl}/${this.getChargeIdentifier(charge)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  partialUpdate(charge: PartialUpdateCharge): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(charge);
    return this.http
      .patch<RestCharge>(`${this.resourceUrl}/${this.getChargeIdentifier(charge)}`, copy, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<RestCharge>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map(res => this.convertResponseFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<RestCharge[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map(res => this.convertResponseArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getChargeIdentifier(charge: Pick<ICharge, 'id'>): number {
    return charge.id;
  }

  compareCharge(o1: Pick<ICharge, 'id'> | null, o2: Pick<ICharge, 'id'> | null): boolean {
    return o1 && o2 ? this.getChargeIdentifier(o1) === this.getChargeIdentifier(o2) : o1 === o2;
  }

  addChargeToCollectionIfMissing<Type extends Pick<ICharge, 'id'>>(
    chargeCollection: Type[],
    ...chargesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const charges: Type[] = chargesToCheck.filter(isPresent);
    if (charges.length > 0) {
      const chargeCollectionIdentifiers = chargeCollection.map(chargeItem => this.getChargeIdentifier(chargeItem)!);
      const chargesToAdd = charges.filter(chargeItem => {
        const chargeIdentifier = this.getChargeIdentifier(chargeItem);
        if (chargeCollectionIdentifiers.includes(chargeIdentifier)) {
          return false;
        }
        chargeCollectionIdentifiers.push(chargeIdentifier);
        return true;
      });
      return [...chargesToAdd, ...chargeCollection];
    }
    return chargeCollection;
  }

  protected convertDateFromClient<T extends ICharge | NewCharge | PartialUpdateCharge>(charge: T): RestOf<T> {
    return {
      ...charge,
      dateCreation: charge.dateCreation?.format(DATE_FORMAT) ?? null,
    };
  }

  protected convertDateFromServer(restCharge: RestCharge): ICharge {
    return {
      ...restCharge,
      dateCreation: restCharge.dateCreation ? dayjs(restCharge.dateCreation) : undefined,
    };
  }

  protected convertResponseFromServer(res: HttpResponse<RestCharge>): HttpResponse<ICharge> {
    return res.clone({
      body: res.body ? this.convertDateFromServer(res.body) : null,
    });
  }

  protected convertResponseArrayFromServer(res: HttpResponse<RestCharge[]>): HttpResponse<ICharge[]> {
    return res.clone({
      body: res.body ? res.body.map(item => this.convertDateFromServer(item)) : null,
    });
  }
}
