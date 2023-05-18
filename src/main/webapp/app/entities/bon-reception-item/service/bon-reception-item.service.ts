import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBonReceptionItem, NewBonReceptionItem } from '../bon-reception-item.model';

export type PartialUpdateBonReceptionItem = Partial<IBonReceptionItem> & Pick<IBonReceptionItem, 'id'>;

export type EntityResponseType = HttpResponse<IBonReceptionItem>;
export type EntityArrayResponseType = HttpResponse<IBonReceptionItem[]>;

@Injectable({ providedIn: 'root' })
export class BonReceptionItemService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/bon-reception-items');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(bonReceptionItem: NewBonReceptionItem): Observable<EntityResponseType> {
    return this.http.post<IBonReceptionItem>(this.resourceUrl, bonReceptionItem, { observe: 'response' });
  }

  update(bonReceptionItem: IBonReceptionItem): Observable<EntityResponseType> {
    return this.http.put<IBonReceptionItem>(
      `${this.resourceUrl}/${this.getBonReceptionItemIdentifier(bonReceptionItem)}`,
      bonReceptionItem,
      { observe: 'response' }
    );
  }

  partialUpdate(bonReceptionItem: PartialUpdateBonReceptionItem): Observable<EntityResponseType> {
    return this.http.patch<IBonReceptionItem>(
      `${this.resourceUrl}/${this.getBonReceptionItemIdentifier(bonReceptionItem)}`,
      bonReceptionItem,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBonReceptionItem>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBonReceptionItem[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getBonReceptionItemIdentifier(bonReceptionItem: Pick<IBonReceptionItem, 'id'>): number {
    return bonReceptionItem.id;
  }

  compareBonReceptionItem(o1: Pick<IBonReceptionItem, 'id'> | null, o2: Pick<IBonReceptionItem, 'id'> | null): boolean {
    return o1 && o2 ? this.getBonReceptionItemIdentifier(o1) === this.getBonReceptionItemIdentifier(o2) : o1 === o2;
  }

  addBonReceptionItemToCollectionIfMissing<Type extends Pick<IBonReceptionItem, 'id'>>(
    bonReceptionItemCollection: Type[],
    ...bonReceptionItemsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const bonReceptionItems: Type[] = bonReceptionItemsToCheck.filter(isPresent);
    if (bonReceptionItems.length > 0) {
      const bonReceptionItemCollectionIdentifiers = bonReceptionItemCollection.map(
        bonReceptionItemItem => this.getBonReceptionItemIdentifier(bonReceptionItemItem)!
      );
      const bonReceptionItemsToAdd = bonReceptionItems.filter(bonReceptionItemItem => {
        const bonReceptionItemIdentifier = this.getBonReceptionItemIdentifier(bonReceptionItemItem);
        if (bonReceptionItemCollectionIdentifiers.includes(bonReceptionItemIdentifier)) {
          return false;
        }
        bonReceptionItemCollectionIdentifiers.push(bonReceptionItemIdentifier);
        return true;
      });
      return [...bonReceptionItemsToAdd, ...bonReceptionItemCollection];
    }
    return bonReceptionItemCollection;
  }
}
