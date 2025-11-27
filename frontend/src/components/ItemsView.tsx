import { type Item } from '../api/generated/openAPIDefinition.schemas';

export default function ItemsView( { items }: Readonly<{ items: Item[] | undefined }> ) {

    console.log( items );
    return (
        <p>test</p>
    )
}
