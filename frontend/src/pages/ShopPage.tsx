import { useParams } from "react-router-dom";

export default function ShopPage() {
    const { shopId } = useParams();
    return <>Shop { shopId }</>
}