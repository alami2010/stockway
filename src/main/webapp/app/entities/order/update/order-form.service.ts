import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IOrder, NewOrder } from '../order.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IOrder for edit and NewOrderFormGroupInput for create.
 */
type OrderFormGroupInput = IOrder | PartialWithRequiredKeyOf<NewOrder>;

type OrderFormDefaults = Pick<NewOrder, 'id' | 'paid'>;

type OrderFormGroupContent = {
  id: FormControl<IOrder['id'] | NewOrder['id']>;
  date: FormControl<IOrder['date']>;
  clientName: FormControl<IOrder['clientName']>;
  clientContact: FormControl<IOrder['clientContact']>;
  subTotal: FormControl<IOrder['subTotal']>;
  vat: FormControl<IOrder['vat']>;
  totalAmount: FormControl<IOrder['totalAmount']>;
  discount: FormControl<IOrder['discount']>;
  grandTotal: FormControl<IOrder['grandTotal']>;
  paid: FormControl<IOrder['paid']>;
  due: FormControl<IOrder['due']>;
  paymentType: FormControl<IOrder['paymentType']>;
  paymentStatus: FormControl<IOrder['paymentStatus']>;
  status: FormControl<IOrder['status']>;
  user: FormControl<IOrder['user']>;
};

export type OrderFormGroup = FormGroup<OrderFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class OrderFormService {
  createOrderFormGroup(order: OrderFormGroupInput = { id: null }): OrderFormGroup {
    const orderRawValue = {
      ...this.getFormDefaults(),
      ...order,
    };
    return new FormGroup<OrderFormGroupContent>({
      id: new FormControl(
        { value: orderRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      date: new FormControl(orderRawValue.date),
      clientName: new FormControl(orderRawValue.clientName),
      clientContact: new FormControl(orderRawValue.clientContact),
      subTotal: new FormControl(orderRawValue.subTotal),
      vat: new FormControl(orderRawValue.vat),
      totalAmount: new FormControl(orderRawValue.totalAmount),
      discount: new FormControl(orderRawValue.discount),
      grandTotal: new FormControl(orderRawValue.grandTotal),
      paid: new FormControl(orderRawValue.paid),
      due: new FormControl(orderRawValue.due),
      paymentType: new FormControl(orderRawValue.paymentType),
      paymentStatus: new FormControl(orderRawValue.paymentStatus),
      status: new FormControl(orderRawValue.status),
      user: new FormControl(orderRawValue.user),
    });
  }

  getOrder(form: OrderFormGroup): IOrder | NewOrder {
    return form.getRawValue() as IOrder | NewOrder;
  }

  resetForm(form: OrderFormGroup, order: OrderFormGroupInput): void {
    const orderRawValue = { ...this.getFormDefaults(), ...order };
    form.reset(
      {
        ...orderRawValue,
        id: { value: orderRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): OrderFormDefaults {
    return {
      id: null,
      paid: false,
    };
  }
}
