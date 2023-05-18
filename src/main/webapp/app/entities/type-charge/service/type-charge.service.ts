import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITypeCharge, NewTypeCharge } from '../type-charge.model';

export type PartialUpdateTypeCharge = Partial<ITypeCharge> & Pick<ITypeCharge, 'id'>;

export type EntityResponseType = HttpResponse<ITypeCharge>;
export type EntityArrayResponseType = HttpResponse<ITypeCharge[]>;

@Injectable({ providedIn: 'root' })
export class TypeChargeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/type-charges');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(typeCharge: NewTypeCharge): Observable<EntityResponseType> {
    return this.http.post<ITypeCharge>(this.resourceUrl, typeCharge, { observe: 'response' });
  }

  update(typeCharge: ITypeCharge): Observable<EntityResponseType> {
    return this.http.put<ITypeCharge>(`${this.resourceUrl}/${this.getTypeChargeIdentifier(typeCharge)}`, typeCharge, {
      observe: 'response',
    });
  }

  partialUpdate(typeCharge: PartialUpdateTypeCharge): Observable<EntityResponseType> {
    return this.http.patch<ITypeCharge>(`${this.resourceUrl}/${this.getTypeChargeIdentifier(typeCharge)}`, typeCharge, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITypeCharge>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITypeCharge[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getTypeChargeIdentifier(typeCharge: Pick<ITypeCharge, 'id'>): number {
    return typeCharge.id;
  }

  compareTypeCharge(o1: Pick<ITypeCharge, 'id'> | null, o2: Pick<ITypeCharge, 'id'> | null): boolean {
    return o1 && o2 ? this.getTypeChargeIdentifier(o1) === this.getTypeChargeIdentifier(o2) : o1 === o2;
  }

  addTypeChargeToCollectionIfMissing<Type extends Pick<ITypeCharge, 'id'>>(
    typeChargeCollection: Type[],
    ...typeChargesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const typeCharges: Type[] = typeChargesToCheck.filter(isPresent);
    if (typeCharges.length > 0) {
      const typeChargeCollectionIdentifiers = typeChargeCollection.map(typeChargeItem => this.getTypeChargeIdentifier(typeChargeItem)!);
      const typeChargesToAdd = typeCharges.filter(typeChargeItem => {
        const typeChargeIdentifier = this.getTypeChargeIdentifier(typeChargeItem);
        if (typeChargeCollectionIdentifiers.includes(typeChargeIdentifier)) {
          return false;
        }
        typeChargeCollectionIdentifiers.push(typeChargeIdentifier);
        return true;
      });
      return [...typeChargesToAdd, ...typeChargeCollection];
    }
    return typeChargeCollection;
  }
}
