import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'utilisateur',
        data: { pageTitle: 'stockwayApp.utilisateur.home.title' },
        loadChildren: () => import('./utilisateur/utilisateur.module').then(m => m.UtilisateurModule),
      },
      {
        path: 'paiement',
        data: { pageTitle: 'stockwayApp.paiement.home.title' },
        loadChildren: () => import('./paiement/paiement.module').then(m => m.PaiementModule),
      },
      {
        path: 'category',
        data: { pageTitle: 'stockwayApp.category.home.title' },
        loadChildren: () => import('./category/category.module').then(m => m.CategoryModule),
      },
      {
        path: 'fournisseur',
        data: { pageTitle: 'stockwayApp.fournisseur.home.title' },
        loadChildren: () => import('./fournisseur/fournisseur.module').then(m => m.FournisseurModule),
      },
      {
        path: 'bon-reception',
        data: { pageTitle: 'stockwayApp.bonReception.home.title' },
        loadChildren: () => import('./bon-reception/bon-reception.module').then(m => m.BonReceptionModule),
      },
      {
        path: 'bon-reception-item',
        data: { pageTitle: 'stockwayApp.bonReceptionItem.home.title' },
        loadChildren: () => import('./bon-reception-item/bon-reception-item.module').then(m => m.BonReceptionItemModule),
      },
      {
        path: 'role',
        data: { pageTitle: 'stockwayApp.role.home.title' },
        loadChildren: () => import('./role/role.module').then(m => m.RoleModule),
      },
      {
        path: 'order',
        data: { pageTitle: 'stockwayApp.order.home.title' },
        loadChildren: () => import('./order/order.module').then(m => m.OrderModule),
      },
      {
        path: 'order-item',
        data: { pageTitle: 'stockwayApp.orderItem.home.title' },
        loadChildren: () => import('./order-item/order-item.module').then(m => m.OrderItemModule),
      },
      {
        path: 'article',
        data: { pageTitle: 'stockwayApp.article.home.title' },
        loadChildren: () => import('./article/article.module').then(m => m.ArticleModule),
      },
      {
        path: 'charge',
        data: { pageTitle: 'stockwayApp.charge.home.title' },
        loadChildren: () => import('./charge/charge.module').then(m => m.ChargeModule),
      },
      {
        path: 'type-charge',
        data: { pageTitle: 'stockwayApp.typeCharge.home.title' },
        loadChildren: () => import('./type-charge/type-charge.module').then(m => m.TypeChargeModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
