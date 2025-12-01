import {
    Dialog,
    DialogContent,
    DialogFooter,
    DialogHeader,
    DialogTitle,
    DialogTrigger
} from "@/components/ui/dialog.tsx";
import type { Item } from "@/api/generated/openAPIDefinition.schemas.ts";
import { Button } from "@/components/ui/button.tsx";
import type { ItemQuantityType } from "@/pages/ItemsPage.tsx";
import { DialogDescription } from "@radix-ui/react-dialog";
import { NavLink, useParams } from "react-router-dom";
import { Separator } from "@/components/ui/separator.tsx";

export default function ItemDialogDetailsView( { item, itemQuantity }: Readonly<{
    item: Item,
    itemQuantity?: ItemQuantityType
}> ) {


    const { shopId } = useParams();

    const formatDate = ( isoDate: string ) => {
        return new Intl.DateTimeFormat( 'de-DE', {
            year: 'numeric',
            month: '2-digit',
            day: '2-digit',
            hour: '2-digit',
            minute: '2-digit'
        } ).format( new Date( isoDate ) );
    };


    return (
        <Dialog>
            <DialogTrigger asChild>
                <Button variant={ "link" }>
                    Details
                </Button>
            </DialogTrigger>
            <DialogContent>
                <DialogHeader>
                    <DialogTitle className={ "text-lg text-accent-foreground font-medium" }>
                        { item.name }
                    </DialogTitle>
                    <DialogDescription className={ "text-muted-foreground" }>
                        Detail view of Item
                    </DialogDescription>
                </DialogHeader>
                <p className={ "text-muted-foreground text-center" }>-- no image available --</p>
                <Separator/>
                <div className={ "grid grid-cols-2 gap-2" }>
                    <p className={ "text-muted-foreground" }>Total #:</p>
                    <p>{ itemQuantity?.totalQuantity }</p>
                    <p className={ "text-muted-foreground" }>Created:</p>
                    <p>{ formatDate( item.createdDate ) }</p>
                    { item.lastModifiedDate && item.lastModifiedDate !== item.createdDate &&
                        <>
                            <p className={ "text-muted-foreground" }>Modified:</p>
                            <p>{ formatDate( item.lastModifiedDate ) }</p>
                        </>
                    }

                </div>

                <Separator/>

                <div className={ "grid grid-cols-2 gap-4" }>
                    {
                        Object.entries( itemQuantity || {} ).map( ( [ warehouseName, quantity ] ) => {
                            if ( warehouseName === "totalQuantity" ) {
                                return null;
                            }
                            return (
                                <div key={ warehouseName }
                                     className={ "flex flex-col text-center gap-2 justify-center items-center" }>
                                    <p key={ warehouseName } className={ "text-center" }>
                                    </p>
                                    <span
                                        className={ "text-muted-foreground" }>{ warehouseName }: </span> { quantity }
                                </div>
                            );
                        } )
                    }
                </div>

                <DialogFooter>
                    <Button asChild className={ "m-auto mt-4" }>
                        <NavLink to={ `/shop/${ shopId }/warehouses` }>See Warehouses</NavLink>
                    </Button>
                </DialogFooter>
            </DialogContent>
        </Dialog>
    )
}
