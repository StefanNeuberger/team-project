import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogTrigger } from "@/components/ui/dialog.tsx";
import type { Item } from "@/api/generated/openAPIDefinition.schemas.ts";
import { Button } from "@/components/ui/button.tsx";
import type { ItemQuantityType } from "@/pages/ItemsPage.tsx";
import { DialogDescription } from "@radix-ui/react-dialog";
import { NavLink } from "react-router-dom";

export default function ItemDialogDetailsView( { item, itemQuantity }: Readonly<{
    item: Item,
    itemQuantity?: ItemQuantityType
}> ) {


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
                        Detail view of item
                    </DialogDescription>
                </DialogHeader>
                <p className={ "text-muted-foreground text-center" }>-- no image available --</p>
                <p className={ "" }><span
                    className={ "text-muted-foreground" }>Total #: </span> { itemQuantity?.totalQuantity ?? "n/a" }
                </p>
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
                                    <Button asChild variant={ "link" }>
                                        <NavLink to={ "/" }>Go to warehouse</NavLink>

                                    </Button>
                                </div>
                            );
                        } )
                    }
                </div>
            </DialogContent>
        </Dialog>
    )
}
