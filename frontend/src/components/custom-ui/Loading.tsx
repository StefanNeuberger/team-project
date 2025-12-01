import { Spinner } from "@/components/ui/spinner.tsx";

export default function Loading( { text }: Readonly<{ text?: string }> ) {
    return (
        <div className="flex flex-col items-center justify-center h-screen gap-3 **:tracking-tight">
            <Spinner className="size-10"></Spinner>
            <div className="text-center leading-5">
                <h2 className="font-bold">{ text ?? "App is loading..." }</h2>
                <small className="text-stone-400">Please do not refresh the page. Getting everything ready for
                    you</small>
            </div>
        </div>
    )
}