import { Audit } from './audit';

export class User extends Audit {
    public id: string;

    public accountNt: string;
    public password: string;

    public firstName: string;
    public lastName: string;
    public fullName: string;

    public roleId: number;
    public active: number;

    public email: string;
    public phone: string;
    public lastLogin: Date;
}
