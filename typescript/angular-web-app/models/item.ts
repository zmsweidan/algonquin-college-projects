import { Audit } from './audit';

export class Item extends Audit {
    public id: number;
    public code: string;
    public nameEn: string;
    public nameFr: string;
    public active: number;
}
